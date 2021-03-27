package com.bottomlord.context;

import com.bottomlord.functions.ThrowableAction;
import com.bottomlord.functions.ThrowableFunction;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.naming.*;
import javax.servlet.ServletContext;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author liveForExp
 */
public class ClassicComponentContext implements ComponentContext {
    private final Logger logger = Logger.getLogger(ClassicComponentContext.class.getName());
    public static final String CONTEXT_NAME = ClassicComponentContext.class.getName();
    private Context envContext;
    private static ServletContext servletContext;
    private ClassLoader classLoader;

    private final Map<Method, Object> preDestroyMethodCache = new LinkedHashMap<>();

    private final Map<String, Object> componentCache = new LinkedHashMap<>();

    public static ClassicComponentContext getInstance() {
        return (ClassicComponentContext) servletContext.getAttribute(CONTEXT_NAME);
    }

    public void init(ServletContext servletContext) throws RuntimeException {
        ClassicComponentContext.servletContext = servletContext;
        servletContext.setAttribute(CONTEXT_NAME, this);
        init();
    }

    @Override
    public void init() {
        initClassLoader();
        initEnvContext();
        instantiateComponents();
        initializeComponents();
        registerShutdownHook();
    }

    @Override
    public void destroy() {
        processPreDestroy();
        clearCache();
        closeEnvContext();
    }

    @Override
    public List<String> getComponentNames() {
        return new ArrayList<>(componentCache.keySet());
    }

    private List<String> listComponentNames() {
        return listComponentNames("/");
    }

    private void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::processPreDestroy));
    }

    private void initClassLoader() {
        this.classLoader = servletContext.getClassLoader();
    }

    /**
     * 通过名称依赖查找
     *
     * @param name 资源名称
     * @param <C>  资源类型
     * @return 资源
     */
    @SuppressWarnings("unchecked")
    public <C> C getComponent(String name) {
        return executeInContext(context -> (C) context.lookup(name), true);
    }

    @SuppressWarnings("unchecked")
    protected <C> C lookupComponent(String name) {
        return (C) componentCache.get(name);
    }

    private void initEnvContext() {
        try {
            this.envContext = (Context) new InitialContext().lookup("java:comp/env");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    private void instantiateComponents() {
        List<String> componentNames = listComponentNames();
        componentNames.forEach(name -> this.componentCache.put(name, getComponent(name)));
    }

    /**
     * 初始化组件
     * <ol>
     *     <li>注入 - {@link javax.annotation.Resource}</li>
     *     <li>初始 - {@link javax.annotation.PostConstruct}</li>
     *     <li>销毁 - {@link javax.annotation.PreDestroy}</li>
     * </ol>
     */
    private void initializeComponents() {
        componentCache.values().forEach(this::initializeComponent);
    }

    private void initializeComponent(Object component) {
        Class<?> componentClass = component.getClass();
        injectComponents(component, componentClass);
        List<Method> candidateMethods = findCandidateMethods(componentClass);
        processPostConstruct(component, candidateMethods);
        processPreDestroyMetadata(component, candidateMethods);
    }

    private void processPreDestroyMetadata(Object component, List<Method> candidateMethods) {
        candidateMethods.stream().filter(method -> method.isAnnotationPresent(PreDestroy.class))
                                .forEach(method -> this.preDestroyMethodCache.put(method, component));
    }

    private List<Method> findCandidateMethods(Class<?> componentClass) {
        return Stream.of(componentClass.getMethods())
                        .filter(method -> !Modifier.isStatic(method.getModifiers()) &&
                                method.getParameterCount() == 0)
                        .collect(Collectors.toList());
    }

    private void processPreDestroy() {
        new HashSet<>(preDestroyMethodCache.keySet()).forEach(method -> {
            Object object = preDestroyMethodCache.remove(method);
            ThrowableFunction.execute(method, m -> m.invoke(object));
        });
    }

    private void injectComponents(Object component, Class<?> componentClass) {
        Stream.of(componentClass.getDeclaredFields())
                .filter(field -> {
                    int mods = field.getModifiers();
                    return !Modifier.isStatic(mods) && field.isAnnotationPresent(Resource.class);
                }).forEach(field -> {
            Resource resource = field.getAnnotation(Resource.class);
            String resourceName = resource.name();
            Object injectedObject = getComponent(resourceName);
            field.setAccessible(true);
            try {
                field.set(component, injectedObject);
            } catch (IllegalAccessException ignored) {
            }
        });
    }

    private void processPostConstruct(Object component, List<Method> candidateMethods) {
        candidateMethods.stream().filter(method -> method.isAnnotationPresent(PostConstruct.class))
                                .forEach(method -> ThrowableFunction.execute(method, m -> m.invoke(component)));
    }

    private List<String> listComponentNames(String name) {
        return executeInContext(context -> {
            NamingEnumeration<NameClassPair> e = executeInContext(context, ctx -> ctx.list(name), true);

            if (e == null) {
                return Collections.emptyList();
            }

            List<String> fullNames = new LinkedList<>();
            while (e.hasMoreElements()) {
                NameClassPair element = e.nextElement();
                String className = element.getClassName();
                Class<?> targetClass = classLoader.loadClass(className);
                if (Context.class.isAssignableFrom(targetClass)) {
                    fullNames.addAll(listComponentNames(element.getName()));
                } else {
                    fullNames.add(name.startsWith("/") ? element.getName() : name + "/" + element.getName());
                }
            }

            return fullNames;
        }, true);
    }

    protected <R> R executeInContext(ThrowableFunction<Context, R> function, boolean ignoredException) {
        return executeInContext(this.envContext, function, ignoredException);
    }

    private <R> R executeInContext(Context context, ThrowableFunction<Context, R> function, boolean ignoredException) {
        R result = null;
        try {
            result = ThrowableFunction.execute(context, function);
        } catch (Throwable e) {
            if (ignoredException) {
                logger.warning(e.getMessage());
            } else {
                throw new RuntimeException();
            }
        }

        return result;
    }

    private void clearCache() {
        preDestroyMethodCache.clear();
        componentCache.clear();
    }

    private void closeEnvContext() {
        close(this.envContext);
    }

    private void close(Context context) {
        if (context != null) {
            ThrowableAction.execute(context::close);
        }
    }
}
