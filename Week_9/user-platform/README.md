# 作业

## 项目运行
 - mvn clean package -U -Dmaven.test.skip=true
 - java -jar .\user-web\target\user-web-v1-SNAPSHOT-war-exec.jar




## 第一周作业

### 要求

- 通过自研 Web MVC 框架实现（可以自己实现）一个用户注册，forward 到一个成功的页面（JSP 用法）/register
- 通过 Controller -> Service -> Repository 实现（数据库实现）
- （非必须）JNDI 的方式获取数据库源（DataSource），在获取 Connection

### 实现

- tomcat插件添加配置`<enableNaming>true</enableNaming>`
- 注册页面：`http://localhost:8080/register`
- 代码实现类：`org.geektimes.projects.user.web.controller.RegisterController`




## 第二周作业

### 要求

- 通过课堂上的简易版依赖注入和依赖查找，实现用户注册功能
- 通过 UserService 实现用户注册，注册用户需要校验
  - Id：必须大于 0 的整数
  - 密码：6-32 位 电话号码: 采用中国大陆方式（11 位校验）

### 实现

1. tomcat7不兼容hibernate-validator:6+，将版本降到5.4.3.Final
    ```xml
       <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-validator</artifactId>
            <version>5.4.3.Final</version>
       </dependency>
    ```
2. 依赖注入实现
    ```java
       @Resource(name = "bean/EntityManager")
       private EntityManager entityManager;
      
       @Resource(name = "bean/UserRepository")
       private UserRepository userRepository;
      
       @Resource(name = "bean/Validator")
       private Validator validator;
      
       ComponentContext.getInstance().getComponent("bean/UserService");
    ```
3. 用户注册校验`org.geektimes.projects.user.service.UserServiceImpl.validateRegister`
    ```java
        @Id
        // 不做处理
        @Min(value = 1, message = "id需大于0")
        @GeneratedValue(strategy = AUTO)
        private Long id;
    
        @Column
        @NotBlank(message = "用户名不能为空", groups = {Register.class, Login.class})
        @Length(max = 16, message = "用户名不能超过16个字符", groups = {Register.class, Login.class})
        private String name;
    
        @Column
        @NotBlank(message = "密码不能为空", groups = {Register.class, Login.class})
        @Length(min = 6, max = 32, message = "密码只支持6~32个字符", groups = {Register.class, Login.class})
        private String password;
    
        @Column
        @NotBlank(message = "邮箱不能为空", groups = {Register.class})
        @Email(message = "邮箱格式错误", groups = {Register.class})
        private String email;
    
        @Column
        @NotBlank(message = "手机号不能为空", groups = {Register.class})
        @Phone(message = "手机号格式错误", groups = {Register.class})
        private String phoneNumber;
    ```
4. 手机号规则校验
    - 注解：`org/geektimes/projects/user/validator/bean/validation/Phone.java`
    - 实现：`org.geektimes.projects.user.validator.bean.validation.PhoneValidator`
    
5. 销毁阶段实现
    - 实现：`org.geektimes.context.ComponentContext.processPreDestroy`
    - 测试：`org.geektimes.projects.user.service.UserServiceImpl.destroy`
    
    
## 第三周作业

### 要求

- 整合`https://jolokia.org/`
  - 实现一个自定义JMX MBean, 通过jolokia做Servlet代理
- 继续完成 Microprofile Config Api 中的实现
  - 扩展 org.eclipse.microprofile.config.spi.ConfigSource 实现，包括 OS 环境变量，以及本地配置文件
  - 扩展 org.eclipse.microprofile.config.spi.Converter 实现，提供 String 类型到简单类型
- 通过 org.eclipse.microprofile.config.Config 读取到当前应用名称
  - 应用名称 property name = "application.name"
  
### 实现(jolokia)
1. maven依赖
    ```xml
        <dependency>
            <groupId>org.jolokia</groupId>
            <artifactId>jolokia-core</artifactId>
            <version>1.6.2</version>
        </dependency>
        <dependency>
            <groupId>org.jolokia</groupId>
            <artifactId>jolokia-client-java</artifactId>
            <version>1.6.2</version>
        </dependency>
    ```
2. servlet配置
    ```xml
        <servlet>
            <servlet-name>jolokia-agent</servlet-name>
            <servlet-class>org.jolokia.http.AgentServlet</servlet-class>
            <load-on-startup>2</load-on-startup>
        </servlet>
    
        <servlet-mapping>
            <servlet-name>jolokia-agent</servlet-name>
            <url-pattern>/jolokia/*</url-pattern>
        </servlet-mapping>
    ```
3. 初始化注册MBean
    org.geektimes.projects.user.web.listener.ManagementBeanInitializerListener
    ```xml
    <listener>
        <listener-class>org.geektimes.projects.user.web.listener.ManagementBeanInitializerListener</listener-class>
    </listener>
    ```
4. 测试类：`org.geektimes.management.demo.JolokiaDemo`
    - MBean列表：`JolokiaDemo.listMBean` http://localhost:8080/jolokia/list
    - MBean属性写入：`JolokiaDemo.writeMBean` http://localhost:8080/jolokia/write/org.geektimes.projects.user.management:type=User/Name/lisi
    - MBean属性读取：`JolokiaDemo.readMBean` http://localhost:8080/jolokia/read/org.geektimes.projects.user.management:type=User/User
    - MBean方法执行：`JolokiaDemo.execMBean` http://localhost:8080/jolokia/exec/org.geektimes.projects.user.management:type=User/toString
    
### 实现(Microprofile Config Api)
1. ConfigSource 扩展
    - META-INF/application.properties配置文件：`org.geektimes.configuration.microprofile.config.source.ApplicationPropertiesConfigSource`
    - 系统环境变量：`org.geektimes.configuration.microprofile.config.source.SystemEnvironmentConfigSource`
2. Converter 定义
    ```text
       org.geektimes.configuration.microprofile.config.converter.StringConvert
       org.geektimes.configuration.microprofile.config.converter.StringToBooleanConvert
       org.geektimes.configuration.microprofile.config.converter.StringToDoubleConvert
       org.geektimes.configuration.microprofile.config.converter.StringToIntegerConvert
       org.geektimes.configuration.microprofile.config.converter.StringToLongConvert
    ```
3. Config 配置读取
    - 实现：`org.geektimes.configuration.microprofile.config.JavaConfig`
    - 获取：`DefaultConfigProviderResolver.instance().getConfig()`
    - 调用：`Config.getValue(String propertyName, Class<T> propertyType)`
4. 测试
    - META-INF/application.properties：`org.geektimes.projects.user.web.listener.TestingListener.testPropertyFromApplicationProperties`
    - 系统环境变量：`org.geektimes.projects.user.web.listener.TestingListener.testPropertyFromSystemEnvironment`
    



## 第四周作业

### 要求

- 完善 my-dependency-injection 模块
    - 脱离 web.xml 配置实现 ComponentContext 自动初始化
    - 使用独立模块并且能够在 user-web 中运行成功
- 完善 my-configuration 模块
    - Config 对象如何能被 my-web-mvc 使用
    - 可能在 ServletContext 获取
    - 如何通过 ThreadLocal 获取
- 去提前阅读 Servlet 规范中 Security 章节（Servlet 容器安全）

### 实现
1. 完善 my-dependency-injection 模块
    - 脱离`web.xml`配置实现`ComponentContext`自动初始化
        
        > org.geektimes.context.servlet.ServletComponentContextInitializer
    - 实现`Controller`从`ComponentContext`注入
        ```java
         // 根据类Class获取所有实例
         List<Controller> controllers = componentContext.getComponents(Controller.class);
         // JNDI配置
         <Resource name="bean/RegisterController" auth="Container"
                  type="org.geektimes.projects.user.web.controller.RegisterController"
                  factory="org.apache.naming.factory.BeanFactory" />  
        ```
    - 配合`my-configuration`模块，实现配置属性`@ConfigProperty`注入
        - 实现：`org.geektimes.context.ComponentContext.injectConfigPropertys`
        - 使用：`@ConfigProperty(name = "application.name", defaultValue = "default-user-web")`
        - 测试：`org.geektimes.projects.user.service.UserServiceImpl.applicationName`
2. 完善 my-configuration 模块
    - 配置初始化：`org.geektimes.configuration.microprofile.config.source.servlet.ServletConfigInitializer`
    - ServletContext：`servletContext.setAttribute(CONFIG_NAME, config);`
    - ThreadLocal实现：
        - RequestContextConfig：请求上下文，实现`Config`接口
            - 构建：build(HttpServletRequest req, HttpServletResponse rep)
            - 获取：get()
            - 销毁：destroy()
            - 获取Request：getRequest()
            - 获取Response：getResponse()
            - 获取请求参数：getValue(String paramName, Class<T> paramType)
        - RequestContextConfigSource：请求数据源，实现`ConfigSource`接口
        - RequestContextFilter：请求上下文过滤器
            
            > 过滤器初始化；org.geektimes.web.servlet.RequestContextFilterInitializer
    



## 第五周作业

### 要求

- 修复本程序 org.geektimes.reactive.streams 包下
- 继续完善 my-rest-client POST 方法
- 读一下 Servlet 3.0 关于 Servlet 异步(可选)
  - AsyncContext

### 实现

1. 完善 my-rest-client POST 方法
    - 实现：`org.geektimes.rest.client.HttpPostInvocation`
    - 测试：`org.geektimes.rest.demo.RestClientDemo`
    



## 第六周作业

### 要求

- my-cache 模块
  - 提供一套抽象 API 实现对象的序列化和反序列化
  - 通过 Lettuce 实现一套 Redis CacheManager 以及 Cache
    
### 实现

1. 提供一套抽象 API 实现对象的序列化和反序列化
    - 实现：`org.geektimes.cache.serializer.CacheSerializer`
        - 默认序列化：DefaultCacheSerializer
        - JSON序列化：JsonCacheSerializer
        - lettuce的序列化：DefaultRedisCodec
    - 测试：`org.geektimes.cache.serializer.CacheSerializerTest`

2. 通过 Lettuce 实现一套 Redis CacheManager 以及 Cache
    - CacheManager实现：`org.geektimes.cache.redis.LettuceCacheManager`
    - Cache实现：`org.geektimes.cache.redis.LettuceCache`
    - 测试：`org.geektimes.cache.redis.LettuceCacheManagerTest`
    



## 第七周作业

### 要求

- 使用 Spring Boot 来实现一个整合 Gitee 或者 Github OAuth2 认证
    - Servlet
    
### 实现

- 实现类
  - GiteeOauthController
  - GiteeOauthServiceImpl

- 运行
    ```text
    mvn clean install -Dmaven.test.skip=true
    java -jar .\user-web\target\user-web-v1-SNAPSHOT-war-exec.jar
    ```
- 入口：`http://localhost:8080/login`
  
    
## 第八周作业

### 要求

如何解决多个 WebSecurityConfigurerAdapter Bean 配置相互冲突的问题？

提示：假设有两个 WebSecurityConfigurerAdapter Bean 定义，并且标注了不同的 @Order，其中一个关闭 CSRF，一个开启 CSRF，那么最终结果如何确定？

背景：Spring Boot 场景下，自动装配以及自定义 Starter 方式非常流行，部分开发人员掌握了 Spring Security 配置方法，并且自定义了自己的实现，解决了 Order 的问题，然而会出现不确定配置因素。

### 实现

https://github.com/cslty/geekbang-lessons/tree/work8



## 第九周作业

### 要求

Spring Cache 与 Redis 整合

  - 如何清除某个 Spring Cache 所有的 Keys 关联的对象
    - 如果 Redis 中心化方案，Redis + Sentinel
    - 如果 Redis 去中心化方案，Redis Cluster
  - 如何将 RedisCacheManager 与 @Cacheable 注解打通

### 实现

1. 清除某个 Spring Cache 所有的 Keys 关联的对象

   - 思路：使用 hash 结构，clear 删除此 hash 结构即可

   - 实现：`org.geektimes.projects.user.cache.RedisCache`

   - 测试：`org.geektimes.cache.RedisCacheTest`

2. 将 RedisCacheManager 与 @Cacheable 注解打通

   - 测试：`org.geektimes.cache.spring.CacheableTest`



## 第十周作业

### 要求

完善`@org.geektimes.projects.user.mybatis.annotation.EnableMyBatis`实现，尽可能多地注入`org.mybatis.spring.SqlSessionFactoryBean`中依赖的组件

### 实现

- 添加`typeAliasesPackage`、`configurationProperties`解析
- 实现：`org.geektimes.projects.user.mybatis.annotation.MyBatisBeanDefinitionRegistrar`
- 测试：`org.geektimes.mybatis.EnableMyBatisTest`