package org.ipman.web.context;

import org.ipman.web.function.ThrowableAction;
import org.ipman.web.function.ThrowableFunction;

import javax.naming.*;
import javax.servlet.ServletContext;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by ipipman on 2021/3/10.
 *
 * @version V1.0
 * @Package org.ipman.web.context
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/3/10 11:37 上午
 */

public class ComponentContext { // 组件上下文（Web 应用全局使用）

    private final static Logger logger = Logger.getLogger(ComponentContext.class.getName());

    // 上下文名称
    public static final String CONTEXT_NAME = ComponentContext.class.getName();

    private static ServletContext servletContext; // 请注意
    // 假设一个 Tomcat JVM 进程，三个 Web Apps，会不会相互冲突？（不会冲突）
    // static 字段是 JVM 缓存吗？（是 ClassLoader 缓存）

    // JNDI 环境目录
    private static final String COMPONENT_ENV_CONTEXT_NAME = "java:comp/env";

    // JNDI 环境目录
    private Context envContext;

    // 当前 Servlet 的 ClassLoader 类加载器
    private ClassLoader classLoader;

    // 所有组件的映射关系
    private final Map<String, Object> componentMap = new LinkedHashMap<>();

    /**
     * 单例模式，获取 CompontContext
     */
    public static ComponentContext getInstance() {
        return (ComponentContext) servletContext.getAttribute(CONTEXT_NAME);
    }

    /**
     * 关闭 JNDI
     */
    private static void close(Context context) {
        if (context != null) {
            ThrowableAction.execute(context::close);
        }
    }

    /*
     * 容器上下文初始化阶段（在 Servlet 初始化完成后被调用）
     *
     * 1. 初始化 JNDI 环境
     * 2. 依赖查找
     * 3. 实例话组件
     *
     */
    public void init(ServletContext servletContext) throws RuntimeException {
        // 绑定组件Servlet Context
        ComponentContext.servletContext = servletContext;
        // 初始化一个单例的 Component Context
        servletContext.setAttribute(CONTEXT_NAME, this);
        // 获取当前 ServletContext （WebApp） ClassLoader
        this.classLoader = servletContext.getClassLoader();

        // TODO 初始化阶段 ...
        // 初始化 JNDI 环境
        initEnvContext();

        // 实例化组件
        instantiateComponents();


    }

    /*
     * 初始化 JNDI 环境
     */
    private void initEnvContext() {
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

    /*
     * 实例化组建
     */
    protected void instantiateComponents() {
        // 遍历获取所有组件的名称
        List<String> componentNames = findAllComponentNames();


    }

    /*
     * 在 JDNI 中查找所有组件
     */
    private List<String> findAllComponentNames() {
        return findComponentNamesByDirectory("/");
    }

    /*
     * 根据目录 在JNDI 中查找所有组件的名称
     */
    protected List<String> findComponentNamesByDirectory(String directory) {
        return executeInContext(context -> {
            // 获取 JNDI 目录下所有的 节点信息
            NamingEnumeration<NameClassPair> e =
                    executeInContext(context, ctx -> ctx.list(directory), true);
            // 目录 - Context
            // 节点 - null
            if (e == null) {
                return Collections.emptyList();
            }

            List<String> fullNames = new LinkedList<>();
            while (e.hasMoreElements()) {
                NameClassPair element = e.nextElement();
                String className = element.getClassName();
                Class<?> targetClazz = classLoader.loadClass(className);
                if (Context.class.isAssignableFrom(targetClazz)) {
                    // 如果当前 JNDI 名称是目录（Context 实现）的话，递归查找
                    fullNames.addAll(findComponentNamesByDirectory(element.getName()));
                } else {
                    // 否则，返回这个 JNDI 节点的绝对路径
                    String fullName = directory.startsWith("/") ?
                            element.getName() : directory + "/" + element.getName();
                    fullNames.add(fullName);
                }
            }
            return fullNames;
        });
    }


    /**
     * 在 JNDI Context 中，通过制定的方法 返回计算结果
     */
    private <E> E executeInContext(ThrowableFunction<Context, E> function) {
        return executeInContext(function, false);
    }

    /**
     * 在 JNDI Context 中，通过制定的方法 返回计算结果
     */
    private <E> E executeInContext(ThrowableFunction<Context, E> function, boolean ignoredException) {
        return executeInContext(this.envContext, function, ignoredException);
    }

    /**
     * 在 JNDI Context 中，通过制定的方法 返回计算结果
     */
    private <E> E executeInContext(final Context context,
                                   ThrowableFunction<Context, E> function, boolean ignoredException) {
        E result = null;
        try {
            result = ThrowableFunction.execute(context, function);
        } catch (Throwable e) {
            // 是否忽略错误
            if (ignoredException) {
                logger.warning(e.getMessage());
            } else {
                throw new RuntimeException(e);
            }
        }
        return result;
    }


}
