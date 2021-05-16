package org.geektimes.context;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.config.spi.Converter;
import org.geektimes.function.ThrowableAction;
import org.geektimes.function.ThrowableFunction;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Java 传统组件上下文（基于 JNDI实现）
 */
public class ClassicComponentContext implements ComponentContext {

    public static final String CONTEXT_NAME = ClassicComponentContext.class.getName();

    public static final String CONFIG_NAME = "org.eclipse.microprofile.config.Config";

    private static final String COMPONENT_ENV_CONTEXT_NAME = "java:comp/env";

    private static final Logger logger = Logger.getLogger(CONTEXT_NAME);

    private static ServletContext servletContext; // 请注意
    // 假设一个 Tomcat JVM 进程，三个 Web Apps，会不会相互冲突？（不会冲突）
    // static 字段是 JVM 缓存吗？（是 ClassLoader 缓存）

//    private static ApplicationContext applicationContext;

//    public void setApplicationContext(ApplicationContext applicationContext){
//        ComponentContext.applicationContext = applicationContext;
//        WebApplicationContextUtils.getRootWebApplicationContext()
//    }

    private Context envContext; // Component Env Context

    private ClassLoader classLoader;

    private Map<String, Object> componentsCache = new LinkedHashMap<>();

    /**
     * @PreDestroy 方法缓存，Key 为标注方法，Value 为方法所属对象
     */
    private Map<Method, Object> preDestroyMethodCache = new LinkedHashMap<>();

    /**
     * 获取 ComponentContext
     *
     * @return
     */
    public static ClassicComponentContext getInstance() {
        return (ClassicComponentContext) servletContext.getAttribute(CONTEXT_NAME);
    }

    public void init(ServletContext servletContext) throws RuntimeException {
        ClassicComponentContext.servletContext = servletContext;
        servletContext.setAttribute(CONTEXT_NAME, this);
        this.init();
    }

    /**
     * 实例化组件
     */
    protected void instantiateComponents() {
        // 遍历获取所有的组件名称
        List<String> componentNames = listAllComponentNames();
        // 通过依赖查找，实例化对象（ Tomcat BeanFactory setter 方法的执行，仅支持简单类型）
        componentNames.forEach(name -> componentsCache.put(name, lookupComponent(name)));
    }

    /**
     * 初始化组件（支持 Java 标准 Commons Annotation 生命周期）
     * <ol>
     *  <li>注入阶段 - {@link Resource}</li>
     *  <li>初始阶段 - {@link PostConstruct}</li>
     *  <li>销毁阶段 - {@link PreDestroy}</li>
     * </ol>
     */
    protected void initializeComponents() {
        componentsCache.values().forEach(this::initializeComponent);
    }

    /**
     * 初始化组件（支持 Java 标准 Commons Annotation 生命周期）
     * <ol>
     *  <li>注入阶段 - {@link Resource}</li>
     *  <li>初始阶段 - {@link PostConstruct}</li>
     *  <li>销毁阶段 - {@link PreDestroy}</li>
     * </ol>
     */
    public void initializeComponent(Object component) {
        Class<?> componentClass = component.getClass();
        // 注入阶段 - {@link Resource}
        injectComponent(component, componentClass);
        // 注入配置属性 - {@link ConfigProperty}
        injectConfigPropertys(component, componentClass);
        // 查询候选方法
        List<Method> candidateMethods = findCandidateMethods(componentClass);
        // 初始阶段 - {@link PostConstruct}
        processPostConstruct(component, candidateMethods);
        // 本阶段处理 {@link PreDestroy} 方法元数据
        processPreDestroyMetadata(component, candidateMethods);
    }

    /**
     * 获取组件类中的候选方法
     *
     * @param componentClass 组件类
     * @return non-null
     */
    private List<Method> findCandidateMethods(Class<?> componentClass) {
        return Stream.of(componentClass.getMethods())                     // public 方法
                .filter(method ->
                        !Modifier.isStatic(method.getModifiers()) &&      // 非 static
                                method.getParameterCount() == 0)          // 无参数
                .collect(Collectors.toList());
    }

    private void registerShutdownHook() {

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            processPreDestroy();

        }));
    }

    public void injectComponent(Object component) {
        injectComponent(component, component.getClass());
    }

    protected void injectComponent(Object component, Class<?> componentClass) {
        Stream.of(componentClass.getDeclaredFields())
                .filter(field -> {
                    int mods = field.getModifiers();
                    return !Modifier.isStatic(mods) &&
                            field.isAnnotationPresent(Resource.class);
                }).forEach(field -> {
            Resource resource = field.getAnnotation(Resource.class);
            String resourceName = resource.name();
            Object injectedObject = lookupComponent(resourceName);
            field.setAccessible(true);
            try {
                // 注入目标对象
                field.set(component, injectedObject);
            } catch (IllegalAccessException e) {
            }
        });
    }

    private void injectConfigPropertys(Object component, Class<?> componentClass) {
        Object attribute = servletContext.getAttribute(CONFIG_NAME);
        if (attribute == null) {
            return;
        }
        Config config = (Config) attribute;
        Stream.of(componentClass.getDeclaredFields())
                .filter(field -> {
                    int mods = field.getModifiers();
                    return !Modifier.isStatic(mods) &&
                            field.isAnnotationPresent(ConfigProperty.class);
                }).forEach(field -> {
            doInjectConfigProperty(component, field, config);
        });
    }

    /**
     * 执行配置注入
     *
     * @param field  字段
     * @param config 配置
     * @throws IllegalArgumentException 字段类型没有匹配的Converter
     */
    private void doInjectConfigProperty(Object component, Field field, Config config) {
        ConfigProperty configProperty = field.getAnnotation(ConfigProperty.class);
        String name = configProperty.name();
        String defaultValue = configProperty.defaultValue();

        Class<?> fieldType = field.getType();
        Object value = config.getValue(name, fieldType);
        // 配置值未获取到，如果有默认值，则注入默认值
        if (value == null && !ConfigProperty.UNCONFIGURED_VALUE.equals(defaultValue)) {
            Converter<?> converter = config.getConverter(fieldType).get();
            value = converter.convert(defaultValue);
        }
        logger.info(String.format("注入属性：%s: %s", name, value));
        if (value != null) {
            field.setAccessible(true);
            try {
                // 注入目标对象
                field.set(component, value);
            } catch (IllegalAccessException e) {
            }
        }
    }

    private void processPostConstruct(Object component, List<Method> candidateMethods) {
        candidateMethods
                .stream()
                .filter(method -> method.isAnnotationPresent(PostConstruct.class))// 标注 @PostConstruct
                .forEach(method -> {
                    // 执行目标方法
                    ThrowableAction.execute(() -> method.invoke(component));
                });
    }

    /**
     * @param component        组件对象
     * @param candidateMethods 候选方法
     * @see #processPreDestroy()
     */
    private void processPreDestroyMetadata(Object component, List<Method> candidateMethods) {
        candidateMethods.stream()
                .filter(method -> method.isAnnotationPresent(PreDestroy.class)) // 标注 @PreDestroy
                .forEach(method -> {
                    preDestroyMethodCache.put(method, component);
                });
    }

    private void processPreDestroy() {
        for (Method preDestroyMethod : preDestroyMethodCache.keySet()) {
            // 移除集合中的对象，防止重复执行 @PreDestroy 方法
            Object component = preDestroyMethodCache.remove(preDestroyMethod);
            // 执行目标方法
            ThrowableAction.execute(() -> preDestroyMethod.invoke(component));
        }
    }

    /**
     * 在 Context 中执行，通过指定 ThrowableFunction 返回计算结果
     *
     * @param function ThrowableFunction
     * @param <R>      返回结果类型
     * @return 返回
     * @see ThrowableFunction#apply(Object)
     */
    protected <R> R executeInContext(ThrowableFunction<Context, R> function) {
        return executeInContext(function, false);
    }

    /**
     * 在 Context 中执行，通过指定 ThrowableFunction 返回计算结果
     *
     * @param function         ThrowableFunction
     * @param ignoredException 是否忽略异常
     * @param <R>              返回结果类型
     * @return 返回
     * @see ThrowableFunction#apply(Object)
     */
    protected <R> R executeInContext(ThrowableFunction<Context, R> function, boolean ignoredException) {
        return executeInContext(this.envContext, function, ignoredException);
    }

    private <R> R executeInContext(Context context, ThrowableFunction<Context, R> function,
                                   boolean ignoredException) {
        R result = null;
        try {
            result = ThrowableFunction.execute(context, function);
        } catch (Throwable e) {
            if (ignoredException) {
                logger.warning(e.getMessage());
            } else {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    public <C> C lookupComponent(String name) {
        return executeInContext(context -> (C) context.lookup(name));
    }


    public <C> C getComponent(String name) {
        return (C) componentsCache.get(name);
    }

    @Override
    public <C> List<C> getComponents(Class<C> classType) {
        List<C> components = new ArrayList<>(componentsCache.size() + 1);
        for (Object value : componentsCache.values()) {
            if (classType.isAssignableFrom(value.getClass())) {
                components.add((C) value);
            }
        }
        return components;
    }

    /**
     * 获取所有的组件名称
     *
     * @return
     */
    public List<String> getComponentNames() {
        return new ArrayList<>(componentsCache.keySet());
    }

    private List<String> listAllComponentNames() {
        return listComponentNames("/");
    }

    protected List<String> listComponentNames(String name) {
        return executeInContext(context -> {
            NamingEnumeration<NameClassPair> e = executeInContext(context, ctx -> ctx.list(name), true);

            // 目录 - Context
            // 节点 -
            if (e == null) { // 当前 JNDI 名称下没有子节点
                return Collections.emptyList();
            }

            List<String> fullNames = new LinkedList<>();
            while (e.hasMoreElements()) {
                NameClassPair element = e.nextElement();
                String className = element.getClassName();
                Class<?> targetClass = classLoader.loadClass(className);
                if (Context.class.isAssignableFrom(targetClass)) {
                    // 如果当前名称是目录（Context 实现类）的话，递归查找
                    fullNames.addAll(listComponentNames(element.getName()));
                } else {
                    // 否则，当前名称绑定目标类型的话话，添加该名称到集合中
                    String fullName = name.startsWith("/") ?
                            element.getName() : name + "/" + element.getName();
                    fullNames.add(fullName);
                }
            }
            return fullNames;
        });
    }

    @Override
    public void init() {
        initClassLoader();
        initEnvContext();
        instantiateComponents();
        initializeComponents();
        registerShutdownHook();
    }

    private void initClassLoader() {
        // 获取当前 ServletContext（WebApp）ClassLoader
        this.classLoader = servletContext.getClassLoader();
    }

    @Override
    public void destroy() throws RuntimeException {
        processPreDestroy();
        clearCache();
        closeEnvContext();
    }

    private void closeEnvContext() {
        close(this.envContext);
    }

    private void clearCache() {
        componentsCache.clear();
        preDestroyMethodCache.clear();
    }

    private void initEnvContext() throws RuntimeException {
        if (this.envContext != null) {
            return;
        }
        Context context = null;
        try {
            context = new InitialContext();
            this.envContext = (Context) context.lookup(COMPONENT_ENV_CONTEXT_NAME);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        } finally {
            close(context);
        }
    }

    private static void close(Context context) {
        if (context != null) {
            ThrowableAction.execute(context::close);
        }
    }
}
