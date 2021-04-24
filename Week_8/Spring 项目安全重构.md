### Spring 项目安全重构



### Java Security

#### 权限 Permission

JDK 内建 API

<img src="https://tva1.sinaimg.cn/large/008eGmZEly1gpn1g4d410j31j20u04qp.jpg"  width="600" align="left" />



- Java.security.Permission

> 这个抽象类是所有权限的祖先。它定义了所有权限所需的基本功能。
>
> 通常通过向构造函数传递一个或多个字符串参数来生成每个权限实例。在有两个参数的常见情况下，第一个参数通常是“目标的名称”(例如权限所针对的文件的名称) ，第二个参数是操作(例如文件的“ read”操作)。通常，可以将一组操作一起指定为逗号分隔的复合字符串。



#### 安全策略

```java
//默认情况下，标准扩展获得所有权限
grant codeBase "file:${{java.ext.dirs}}/*" {
  	permission java.security.AllPermission;
};
```

```java
grant {
    //允许任何线程使用java.lang.Thread.stop()
    //不带参数的方法。
    //请注意，默认情况下，此权限仅授予
    //向后兼容。
    //强烈建议您删除此权限
    //或进一步将其限制为代码源
    //你指定的，因为Thread.stop（）可能不安全。
    //参见API规范java.lang.Thread.stop（）了解更多
    //信息。
    permission java.lang.RuntimePermission "stopThread";
    //允许任何人监听动态端口
    permission java.net.SocketPermission "localhost:0", "listen";
    //任何人都可以阅读的“标准”属性
    permission java.util.PropertyPermission "java.version", "read";
    permission java.util.PropertyPermission "java.vendor", "read";
    permission java.util.PropertyPermission "java.vendor.url", "read";
    permission java.util.PropertyPermission "java.class.version", "read";
    permission java.util.PropertyPermission "os.name", "read";
    permission java.util.PropertyPermission "os.version", "read";
    permission java.util.PropertyPermission "os.arch", "read";
    permission java.util.PropertyPermission "file.separator", "read";
    permission java.util.PropertyPermission "path.separator", "read";
    permission java.util.PropertyPermission "line.separator", "read";

    permission java.util.PropertyPermission "java.specification.version", "read";
    permission java.util.PropertyPermission "java.specification.vendor", "read";
    permission java.util.PropertyPermission "java.specification.name", "read";

    permission java.util.PropertyPermission "java.vm.specification.version", "read";
    permission java.util.PropertyPermission "java.vm.specification.vendor", "read";
    permission java.util.PropertyPermission "java.vm.specification.name", "read";
    permission java.util.PropertyPermission "java.vm.version", "read";
    permission java.util.PropertyPermission "java.vm.vendor", "read";
    permission java.util.PropertyPermission "java.vm.name", "read";
};
```



#### 安全策略配置文件

文件路径： %JAVA_HOME%/jre/lib/security/java.policy

> java.ext.dirs 系统属性表示：JVM扩展的 ClassLoader 路径
>
> - Bootstrap ClassLoader
>   - System ClassLoader
>     - App ClassLoader
>       - Ext ClassLoader （JDK1.9 开始淘汰）



#### 激活安全管理器 - SecurityManager

```java
System.setSecurityManager(new SecurityManager());
```



#### 自定义权限

需要用到这些API：

- Java  安全校验方法 - SecurityManager#checkPremission(java.security.Permission)

Java .security.AccessController#checkPremission(java.security.Permission)

- Java 鉴权方法 - java.security.AccessController#doPrivileged(java.security.PrivilegedAction) 以及重载



####  相关内容

##### 软件开发原则

- 最小依赖
  - 依赖第三方 jar
  - 依赖 JDK
  - 依赖 OS
  - 依赖 JVM



#### Spring Stack 三大方向

- 传统的 Java EE 技术栈（稳定）
  - JDBC、JMS、JPA 等等
- Reactive 技术栈（不稳定）
  - Reactive（Reactor）+ Java EE
    - Mono、Flux
- Cloud-Native 技术栈（趋势）
  - Micro = Services
    - Spring Boot
    - Spring Clound
  - Function
    - Spirng Cloud Function
    - Spring Cloud Stream
  - Natvie
    - Spring Native



### Spring Security

#### 安全设计模式

基于拦截模式实现，比如利用 AOP，Servlet Filter



#### Servelet 技术栈

基于 Servlet 规范中的 Filter API 实现

第一部分来自于 Spring Web 实现

第二部分来自于 Spring Security 实现



#### Spring Web 实现

**org.springframework.web.filter.DelegatingFilterProxy**

- 使用场景
  - Spring Web 为应用提供了一个 Servlet 容器 Filter 组件和 Spring Filter Bean 桥接的管道

Delegate - 实际的 Filter

- targetBeanName Filter Bean
- delegating Filter 对象



**org.springframework.web.filter.GenericFilterBean**

生命周期

- BeanNameAware
- EnvironmentAware
- EnvironmentCapable
- ServletContextAware
- InitializingBean
- DisposableBean

- 实现特点
  - 通常扩展 GenericFilterBean 并非一个传统 Spring Bean，但是他需要 Spring Bean 生命周期。 
- 属性绑定
  - 将 FilterConfig 配置绑定到 GenricFilterBean 实现类的属性上



#### FilterChainProxy

作为 DelegatingFilterProxy 中的 delegate Bean 对象，并且再次将 HTTP 请求委派给 SecurityFilterChain 所关联的多个Filter

她们之前的关系：

Servlet 容器 -> DelegatingFilterProxy -> FilterChainProxy -> N * SecurityFilterChain -> M * Filter



<img src="https://tva1.sinaimg.cn/large/008i3skNly1gpv5l9x8qwj31560u0x0w.jpg"  width="600" align="left"/>



#### SecurityFilterChain

被FilterChainProxy 来委派，并且 FilterChainProxy 与 SecurityFilterChain 数量关系是 1:N

<img src="https://tva1.sinaimg.cn/large/008i3skNly1gpv5qpkyflj30k50e8q90.jpg"  wdith="500" align="left" />

SecurityFilterChain 中关联的 M 个 Filter 实现，并且 Filter 实现之前是有序（因为它通过 List 类型作为 getFilters() 方法返回值）

> 思考：SecurityFilterChain 中关联的 M 个 Filter 实现，是通过 Servlet 容器管理生命周期，还是通过 Spring IoC容器



#### Security Filters

被安插到 SecurityFilterChain 之重，并且内建实现存在执行优先次序

