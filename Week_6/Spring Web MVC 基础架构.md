

### Spring Web MVC 基础架构重构

#### Spring Web MVC REST 处理流程

<img src="https://tva1.sinaimg.cn/large/008eGmZEly1gpezhkx70rj31oo0l6dta.jpg" style="zoom:50%;" align="left"/>





#### 服务端试图渲染

- 模版引擎
  - JSP 
    -  基于 Servlet 引擎 - JspServlet
  - Thymeleaf
  - Freemarker
  - Velocity
- 模版设计模式
  - 模版
    - 表达式 - 逻辑、循环、赋值等指令
    - 占位符（变量）- EL 表达式
      - 变量名关联变量
      - 变量利用 JavaBeans
    - 上下文 - Key-Value 的存储结构
      - 举例：Spring MVC Model  类似于 Map
    - 执行引擎
      - Velocity - TemplateEngine



#### REST 服务端

Spirng MVC  模式 -> MC， 对 V 不敏感

在早期 Spring Web MVC -> JsonView

- HTTP 方法
  - GET
  - POST
- URL
  - URL Template
    - 常量路径 - /Users/list
    - 变量路径- /Users/{id}
      - Spring MVC - @PathVariable
      - JAX-RS - @PathParam
- 内容协商
  - 客户端请求服务端以某种媒体类型来响应
  - 对于服务端而言，影响内容文本和二进制
  - REST application/json 或者 application/xml
  - 客户端而言，请求头 Accept
  - 服务端而言，响应头 Content-Type
  - 有一个通用的 API -  MediaType （主类型/子类型）
- 序列化和反序列化
  - 反序列化：HTTP 请求 -> 文本或者二进制 -> POJO
  - 序列化：POJO -> 文本或者二进制 -> HTTP 请求



#### SpringMVC 核心组件

<img src="https://tva1.sinaimg.cn/large/008eGmZEly1gpevv9dme4j31620s4h3s.jpg" style="zoom:50%;" align="left"/>





#### 处理方法映射：HandlerMapping

Handler = HandlerMehtod  == 应用标注 @RequestMapping 或者派生注解 Java 方法

```java
@Controller
public class AbcController {
    @RequestMapping(value = "/abc1")
    public String abc1(@RequestParam Strin name){
       return "abc";
    }
  
    @RequestMapping(value = "/abc2")
    public View abc2(@RequestParam Strin name){
 				return View("abc");
    }
  
  	@RequestMapping(value = "/abc3")
    public View abc3(@RequestParam Strin name, ModelAndView modelAndView){
				modelAndView.setView("abc");
    }
}
```



Spirng Web MVC 基于 Servlet 引擎，参数 HttpServletRequest 和 HttpServletResponse

以 JSP 为例，Spring Web MVC 的跳转等价于 DispatcherServlet forward 到 JspServlet

@Component

@Controller

@Service

@Respository

> HTTP 请求 -> URI -> @RequestMapping -> 执行具体方法



#### 模型和试图组合类 - ModelAndView

组成成员：

- 试图对象 - view 字段（Object）：一种可以能是 view 对象 ，一种是 String 对象
- 模型对象 - Model 对象
  - org.springframework.ui.ModelMap 类继承了 -  LinkedHashMap
  - org.springframework.ui.Model 接口



#### 方法适配器：HandlerAdapter

HandlerMethod 与 Servlet API 做适配，利用 HandlerMethod 中执行结果，去控制适配 HttpServletRequest 和 HttpServletResponse

HandlerAdapter 存在多种实现，比如注解实现 ： @RequestMapping

- org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter（3.1+）



HandlerAdapter 提高优先级

- 内部实现通常想增加 setOrder(int) 方法
- 自定义组件通过实现 Ordered 接口



#### 方法执行： HandlerExecutionChain（责任链模式）

责任链条模式：类似 Filter 和 Servlet 关系， FilterChain

#####  潜在风险

``` java
@Nullable
protected final HandlerInterceptor[] getAdaptedInterceptors() {
  	return !this.adaptedInterceptors.isEmpty() ? (HandlerInterceptor[])this.adaptedInterceptors.toArray(new HandlerInterceptor[0]) : null;
}
```

- Interceptors 是一个可以被修改的数组，比如：getAdaptedInterceptors



#### 处理方法 HandlerMethod 拦截器 - HandlerInterceptor

- 前置判断 - preHandler

  - 当且仅当方法返回 true 时，执行 HandlerMethod 与 Servlet API 做适配

- 后置处理 - postHandler

  - HandlerMethod 已经被执行，其执行结果为 ModelAndView
    - 当 ModelAndView 参数为空， 说明是非试图渲染，即REST 场景（@Since 2.5）
    - 否则就试图渲染

- 完成回调 - aferCompletion

  - 正常执行顺序 preHanler -> handler -> postHandler -> afterCompletion

    

#### 请求属性 - org.springframework.web.context.request.RequestAttributes

Bean Scope:

- Singleton (默认)
- Prototype（原型）
- Request （Bean 存放在 HttpServletRequest）
- Session （Bean 存放在 HttpSession）
- Application （Bean 存放在 ServletContext）
  - Bean 存放在 ServletContext 主要给 Servlet 试图
    - Spring IoC 关联 Servelt - DispatcherServlet
    - JSP 的Servlet - JspServlet
  - Servlet 应用对应一个 ServletContext，但是它可能对应多个 SpringBeanFactory（ApplictionContext）
    - ContextLoaderListener（Root ApplicationContext）
    - DispatcherServlet（Child ApplicationContext）



#### 自定义组件 ：WebMvcConfigurer

Spring 5 之前通过使用 WebMvcConfigurerAdpater 类

Spirng 5 开始使用 WebMvcConfigurer 类

WebMvcConfigurer配置类其实是`Spring`内部的一种配置方式，采用`JavaBean`的形式来代替传统的`xml`配置文件形式进行针对框架个性化定制，可以自定义一些Handler，Interceptor，ViewResolver，MessageConverter。基于java-based方式的spring mvc配置，需要创建一个**配置**类并实现**`WebMvcConfigurer`** 接口



WebMvcConfigurer 引导类 - DelegatingWebMvcConfiguration

DelegatingWebMvcConfiguration（@Configuration Class） 会被 @EnableWebMvc 引导

```java
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({DelegatingWebMvcConfiguration.class})
public @interface EnableWebMvc {
}
```

> @Configuration 标注的类会被 CGLib 提升
>
> @Import 将一个普通类， 注册为 Srping Bean ，类似于 Configuration Class



DelegatingWebMvcConfiguration 继承了 WebMvcConfigurationSupport

里面定义了 HandlerAdpater Bean - RequestHandlerAdapter



#### 注解驱动

<img src="https://tva1.sinaimg.cn/large/008eGmZEly1gpeyehc6zsj30vk0u0qum.jpg" align="left" width="300" height="300" />





#### Servlet 与 JSP 基础 - JSP 四大范围

- 页面范围：仅限于当前 JSP 页面
  - PageContext#setAttribute(String,  Object)
- 请求范围：当前请求（可能垮多个 Servlet）
  - 请求上下文
  - RequestContext#setAttribute(String,  Object)
- 会话范围：用户 HttpSession
  - 会话上下文
  - SessionContext#setAttribute(String,  Object)
- 应用范围： ServletContext
  - ServletContext#setAttribute(String,  Object)





