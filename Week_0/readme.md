## 极客大学 - 小马哥的 Java 项目实战营，ipipman助教课前笔记目录

##  [第一周任务（需求分析和开发）](https://github.com/ipipman/GeekBangJava0Assistant/tree/master/Week_0 "第一周（需求分析和开发）")
> - 项目模块：“用户注册”、“用户登录”以及“用户管理”等模块构建；
> - 规范运用：使用Java SE 和 Java EE 技术栈实现业务需求；
> - 页面渲染：基于JSP、EL 以及 JSTL 技术实现 Java WEB 服务端视图渲染；
> - 服务通讯：基于JAX-RS 实现同步服务通讯，基于JMS 提供异步服务通讯能力；

------------

### （一）J2EE 
### 什么是J2EE？
在企业应用中，都有一些通用企业需求模块，如数据库连接、邮件服务、事务处理等，既然很多企业级应用都需要这些模块，一些大公司便开发了自己的通用模块服务，中间件。
这样一来，就**避免了重复开发、开发周期、代码可靠性差等问题**。但是，各公司的中间件不兼容的问题就出现了，用户无法将他们组装在一起为自己服务，于是“标准”就应运而生了。

**J2EE就是基于JAVA技术的一系列标准**。
J2EE是Java2企业版（Java 2Platform Enterprise Edition），核心是一组技术规范与指南，**其中所包含的各个组件，服务架构和技术层次，都有共同的标准及规格，让各种依赖J2EE架构的不同平台之间，存在良好的兼容性**。


------------

### J2EE的13种规范
[![https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/geekbang/1311614079083_.pic.jpg](https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/geekbang/1311614079083_.pic.jpg "https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/geekbang/1311614079083_.pic.jpg")](https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/geekbang/1311614079083_.pic.jpg "https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/geekbang/1311614079083_.pic.jpg")


#### 1. JDBC（Java Database Connectivity）
**JDBC 是以统一方式访问数据库的API**

它提供了独立于平台的数据库访问，也就是说，有了JDBC API，我们就不必为了访问Oracle数据库专门写一个程序，而为了访问Mysql数据库又单独写一个程序等等。
只需要用JDBC API 写一个程序就够了，它可以向相应数据库发送SQL 调用，**JDBC是Java 应用程序与各种不同数据库之间进行对话的方法的机制**，简单地说，它做了三件事：

> - **与数据库建立连接**
> - **发送操作数据库的语句**
> - **处理结果**

------------

#### 2. JNDI（JavaName and Directory Interface）
**JNDI是一组在Java应用中访问命名和目录服务的API**

**命名服务将名称和对象联系起来**，**我们即可用名称访问对象**，J**NDI 允许吧名称同Java 对象或资源关联起来，建立逻辑关联，而不必知道对象或资源的物理ID。**
JNDI为开发人员提供了**查找和访问各种命名和目录服务的通用**，统一了接口。

利用**JNDI的命名与服务功能可满足企业级API 对命名与服务的访问**，诸如：EJB、JMS、JDBC等。

JNDI和JDBC类似，都是构建在抽象层上
因为它提供了标准的独立于系统的API，这些API构建在命名系统之上，这一层**有助于将应用与实际数据源分离**，因此不管访问的LDAP、RMI还是DNS，也就是说，JNDI独立于目录服务的具体实现，**只要有目录的服务提供接口或驱动，就可以使用目录**

------------

#### 3. EJB （Enterprise Java Bean）
**J2EE将业务逻辑从客户端软件中抽取出来，分装在一个组建中。**

这个组件运行在一个独立的服务上，**客户端软件通过网络调用组件提供的服务以实现业务逻辑**，而客户端软件功能只负责发送调用请求和显示处理结果。

在J2EE中，这个**运行在一个独立的服务器上，并封装了业务逻辑的组建就是EJB 组件**。其实就是把原来放到客户端实现的代码放到服务器端，并以来RMI 进行通信。

------------

#### 4. RMI （Remote Method Invoke）
**是一组用户开发分布式应用程序的API**

这一协议**调用远程对象上的方法使用了序列化的方式**，**在客户端与服务端之间传递数据**。使得原先在同一操作系统方法调用，变成了不同操作系统之间程序的方法调用。**即RMI 机制实现了程序组件在不同操作系统之间通信**。

RMI属于EJB 更底层的通信协议

RMI / JNI：RMI 可以利用标准Java本机方法接口与现有的和原有的系统相连接
RMI / JDBC：RMI 利用标准JDBC 包与现有的关系型数据库连接

RMI实现了与飞Java 语言的现有服务进行通信

------------

#### 5. Java IDL / CORBA （Common Object Request Broker Architecture）
**Java 接口定义语言，公用对象请求代理程序体系结构**

在Java IDL的支持下，开发人员可以将Java 和 CORBA 集成在一起。他们可以创建Java 对象并使之可在CORBA ORB中展开，或者他们呢还可以创建Java 类并作为和其它ORB 一起展开的CORBA 对象的客户。后一种方式提供了另外一种途径，通过它Java 可以被用于拉新的应用和旧的系统相集成

CORBA 是面向对象标准的第一步，有了这个标准，软件的实现与工作环境对用户和开发者不再重要，可以把精力更多地放在本地系统的实现与优化上

------------

#### 6. JSP （JAVA Server Pages）
**JSP页面 = HTML+Java，其根本是一个简化的Servlet设计**

服务器在页面被客户端请求后，对这些Java 代码进行处理，**然后将执行结果连同连同原HTML代码，生成的新HTML页面返回给客户端浏览器**

------------

#### 7. Java Servlet
**Servlet 是一种小型的Java 程序，扩展了Web 服务器的功能，作为一种服务器的应用，当请求时开始执行。**

**Servlet 提供的功能大多和 JSP 类似**，不过，**JSP 通常是大多数的HTML 代码中潜入少量的Java 代码**。**而Servlet 全部由Java 写成并生成 HTML**

------------

#### 8. XML
**XML 是一个用来定义其它标记语言的语言，可用做数据共享。**

XML 的发展和Java 是相互独立的。
不过Java 具有的相同目标就是**跨平台**。通过Java 和 XML结合，我们可以得到一个完全和平台无关的解决方案

------------

#### 9. JMS （Java Message Service）
**它是一种与厂商无关的API，用来访问消息收发系统消息**

它类似于JDBC，JDBC是可以用来访问不同关系数据的API，而JMS 则提供同样与厂商无关的访问消息收发服务的方法，这样就可以通过消息收发服务实现从一个JMS 客户机向另一个JMS 客户端机发送消息。

**JMS 是Java 平台上有关面向消息中间件的技术规范**

------------

#### 10. JTA （Java Transaction API）
**定义了一种标准API，应用程序由此可以访问各种事务监控**

它允许**应用程序执行分布式事务处理**，在两个或多个网络计算机资源上访问并且更新数据，JTA 和 JTS 为J2EE 平台提供了分布式服务。
JTA 事务比JDBC 事务更强大，一个JTA 事务可以有多个参与者，而一个JDBC 事务则被限定在一个单一的数据库连接

------------

#### 11. JTS （Java Transaction Service）
**JTS 是 CORBA OTS 事务监控器的一个基本实现**

**JTS 指定了一个事务管理器的实现（Transaction Manager）**，这个管理器在一个高级别上支持JTA 规范，并且在一个低级别上实现了OMGOTS 规范的Java 映射。

一个JTS 事务管理器为应用服务器、资源管理器、独立的应用和通信资源管理器提供事务服务

------------

#### 12. JavaMail
**用于访问邮件服务器的API，提供了一套邮件服务器的抽象类**

------------

#### 13. JAF（Java Beans Activation Framework）
**JAF 是一个专用的数据处理框架**

它用于封装数据，并为应用程序提供访问和操作数据的接口，也就是说JAF 让Java 程序知道怎么对一个数据源进行查看、编辑、打印等

JavaMail利用JAF 来处理MIME 编码的邮件附件

------------

### （二）Servlet
#### Servlet 是什么？

**Java Servlet 是运行在 Web 服务器或应用服务器上的程序**，它是作为来自 Web 浏览器或其他 HTTP 客户端的请求和 HTTP 服务器上的数据库或应用程序之间的**中间层**。

使用 Servlet，您可以收集来自网页表单的用户输入，呈现来自数据库或者其他源的记录，还可以动态创建网页。

[![https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/geekbang/1341614237301_.pic.jpg](https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/geekbang/1341614237301_.pic.jpg "https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/geekbang/1341614237301_.pic.jpg")](https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/geekbang/1341614237301_.pic.jpg "https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/geekbang/1341614237301_.pic.jpg")

------------

#### Servlet 的主要任务

> - **读取客户端（浏览器）发送的显式的数据。这包括网页上的 HTML 表单，或者也可以是来自 applet 或自定义的 HTTP 客户端程序的表单**
> - **读取客户端（浏览器）发送的隐式的 HTTP 请求数据。这包括 cookies、媒体类型和浏览器能理解的压缩格式等等**
> - **处理数据并生成结果。这个过程可能需要访问数据库，执行 RMI 或 CORBA 调用，调用 Web 服务，或者直接计算得出对应的响应**
> - **发送显式的数据（即文档）到客户端（浏览器）。该文档的格式可以是多种多样的，包括文本文件（HTML 或 XML）、二进制文件（GIF 图像）、Excel 等**
> - **发送隐式的 HTTP 响应到客户端（浏览器）。这包括告诉浏览器或其他客户端被返回的文档类型（例如 HTML），设置 cookies 和缓存参数，以及其他类似的任务**

------------

#### Servlet 版本

[![https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/geekbang/1371614255303_.pic_hd.jpg](https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/geekbang/1371614255303_.pic_hd.jpg "https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/geekbang/1371614255303_.pic_hd.jpg")](https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/geekbang/1371614255303_.pic_hd.jpg "https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/geekbang/1371614255303_.pic_hd.jpg")

------------

#### Servlet 包

**Java Servlet 是运行在带有支持 Java Servlet 规范的解释器的 web 服务器上的 Java 类**

Servlet 可以使用 **javax.servlet** 和 **javax.servlet.http** 包创建，它是 Java 企业版的标准组成部分，Java 企业版是支持大型开发项目的 Java 类库的扩展版本

这些类实现 Java Servlet 和 JSP 规范
Java Servlet 就像任何其他的 Java 类一样已经被创建和编译。在您安装 Servlet 包并把它们添加到您的计算机上的 Classpath 类路径中之后，您就可以通过 JDK 的 Java 编译器或任何其他编译器来编译 Servlet

------------

### Servlet 生命周期

Servlet 生命周期可被定义为**从创建直到毁灭的整个过程**。以下是 Servlet 遵循的过程

> - **Servlet 通过调用 init () 方法进行初始化**
> - **Servlet 调用 service() 方法来处理客户端的请求**
> - **Servlet 通过调用 destroy() 方法终止（结束）**
> - **最后，Servlet 是由 JVM 的垃圾回收器进行垃圾回收的**

------------

#### init() 方法
**init 方法被设计成只调用一次**。它在第一次**创建 Servlet 时被调用**，**在后续每次用户请求时不再调用**。因此，**它是用于一次性初始化**，就像 Applet 的 init 方法一样。

**Servlet 创建于用户第一次调用对应于该 Servlet 的 URL 时**，但是您也可以指定 Servlet 在服务器第一次启动时被加载。

当用户调用一个 Servlet 时，就会创建一个 Servlet 实例，**每一个用户请求都会产生一个新的线程**，适当的时候移交给 doGet 或 doPost 方法。init() 方法简单地创建或加载一些数据，这些数据将被用于 Servlet 的整个生命周期。
```java
public void init() throws ServletException {
      // 初始化代码...
}
```

------------

#### service() 方法
service() 方法是执行实际任务的主要方法。Servlet 容器（即 Web 服务器）调用 service() 方法来处理来自客户端（浏览器）的请求，并把格式化的响应写回给客户端。

每次服务器接收到一个 Servlet 请求时，**服务器会产生一个新的线程并调用服务**。service() 方法检查 HTTP 请求类型（GET、POST、PUT、DELETE 等），并在适当的时候调用 doGet、doPost、doPut，doDelete 等方法。

service() 方法由容器调用，service 方法在适当的时候调用 doGet、doPost、doPut、doDelete 等方法。所以，您**不用对 service() 方法做任何动作，您只需要根据来自客户端的请求类型来重载 doGet() 或 doPost() 即可**。
```java
public void service(ServletRequest request, 
                        ServletResponse response) 
          throws ServletException, IOException{
}
```

------------

#### doGet() 方法
GET 请求来自于一个 URL 的正常请求，或者来自于一个未指定 METHOD 的 HTML 表单，它由 doGet() 方法处理
```java
public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws ServletException, IOException {
        // Servlet 代码
}

```

------------

#### doPost () 方法
POST 请求来自于一个特别指定了 METHOD 为 POST 的 HTML 表单，它由 doPost() 方法处理。
```java
public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
        throws ServletException, IOException {
        // Servlet 代码
}
```

------------

#### destroy  () 方法

**destroy() 方法只会被调用一次**，在 **Servlet 生命周期结束时被调用**。destroy() 方法可以让您的 Servlet 关闭数据库连接、停止后台线程、把 Cookie 列表或点击计数器写入到磁盘，并执行其他类似的清理活动。

在调用 destroy() 方法之后，**servlet 对象被标记为垃圾回收**
```java
public void destroy() {
	// 终止化代码...
}
```


------------

#### Servlet 架构
> - ** 第一个到达服务器的 HTTP 请求被委派到 Servlet 容器**
> - **Servlet 容器在调用 service() 方法之前加载 Servlet**
> - **然后 Servlet 容器处理由多个线程产生的多个请求，每个线程执行一个单一的 Servlet 实例的 service() 方法**

[![https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/geekbang/1361614244886_.pic.jpg](https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/geekbang/1361614244886_.pic.jpg "https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/geekbang/1361614244886_.pic.jpg")](https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/geekbang/1361614244886_.pic.jpg "https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/geekbang/1361614244886_.pic.jpg")

------------

### Servlet 实例
#### （一）JavaEE提供了Servlet API，我们使用Servlet API编写自己的Servlet来处理HTTP请求，Web服务器实现Servlet API接口
```java

// WebServlet注解表示这是一个Servlet，并映射到地址/:
@WebServlet(urlPatterns = "/")
// 一个Servlet总是继承自HttpServlet，然后覆写doGet()或doPost()方法
public class HelloServlet extends HttpServlet {

    // init 方法被设计成只调用一次。它在第一次创建 Servlet 时被调用，在后续每次用户请求时不再调用。
    @Override
    public void init(ServletConfig config) throws ServletException {
        System.out.println("Servlet 初始化");
    }

    // 每次服务器接收到一个 Servlet 请求时，服务器会产生一个新的线程并调用服务。
    @Override
    public void service(ServletRequest var1, ServletResponse var2) throws ServletException, IOException{
        HttpServletRequest request = (HttpServletRequest) var1;
        System.out.println("Servlet Request Method = " + request.getMethod());
    }

    // doGet()方法传入了HttpServletRequest和HttpServletResponse两个对象，分别代表HTTP请求和响应
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 设置响应类型:
        resp.setContentType("text/html");
        // 获取输出流:
        PrintWriter pw = resp.getWriter();
        // 写入响应:
        pw.write("<h1>Hello, world!</h1>");
        // 最后不要忘记flush强制输出:
        pw.flush();
    }

    // destroy() 方法只会被调用一次，在 Servlet 生命周期结束时被调用
    @Override
    public void destroy() {
        System.out.println("Servlet 销毁");
    }
}

```

#### （二）Servlet API是一个jar包，我们需要通过Maven来引入它，才能正常编译。编写pom.xml文件如下

```java
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.example</groupId>
    <artifactId>week0-user-admin-sample</artifactId>
    <version>1.0-SNAPSHOT</version>

    <!--  打包类型不是jar，而是war，表示Java Web Application Archive  -->
    <packaging>war</packaging>

    <build>
        <finalName>hello</finalName>

        <plugins>
            <plugin>
                <groupId>org.mortbay.jetty</groupId>
                <artifactId>maven-jetty-plugin</artifactId>
                <version>6.1.7</version>
                <configuration>
                    <webAppSourceDirectory>${project.build.directory}/${pom.artifactId}-${pom.version}
                    </webAppSourceDirectory>
                    <contextPath>/</contextPath>
                </configuration>
            </plugin>
        </plugins>
    </build>


    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
        <java.version>8</java.version>
    </properties>

    <dependencies>
        <!--   Servlet API   -->
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>4.0.0</version>
            <!--  <scope>指定为provided，表示编译时使用，但不会打包到.war文件中，因为运行期Web服务器本身已经提供了Servlet API相关的jar包 -->
            <scope>provided</scope>
        </dependency>
    </dependencies>


</project>
```

#### （三）在工程目录下创建一个web.xml描述文件，放到src/main/webapp/WEB-INF目录下（固定目录结构，不要修改路径，注意大小写）。文件内容可以固定如下：

    <!DOCTYPE web-app PUBLIC
     "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
     "http://java.sun.com/dtd/web-app_2_3.dtd">
    <web-app>
      <display-name>Archetype Created Web Application</display-name>
    </web-app>

#### （四）准备Tomcat服务器，在IDEA中配置启动环境
下载最新版本的Tomcat：[http://tomcat.apache.org/](http://tomcat.apache.org/ "http://tomcat.apache.org/")

[![https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/geekbang/1351614243759_.pic.jpg](https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/geekbang/1351614243759_.pic.jpg "https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/geekbang/1351614243759_.pic.jpg")](https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/geekbang/1351614243759_.pic.jpg "https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/geekbang/1351614243759_.pic.jpg")


启动Tomcat，进行测试 ...

------------


### （三）JSP
#### 什么是Java Server Pages?
JSP全称Java Server Pages，**是一种动态网页开发技术**。它使用JSP标签在HTML网页中插入Java代码。标签通常以<%开头以%>结束。

**JSP是一种Java servlet**，主要用于实现Java web应用程序的用户界面部分。网页开发者们通过结合HTML代码、XHTML代码、XML元素以及嵌入JSP操作和命令来编写JSP。

JSP通过网页表单获取用户输入数据、访问数据库及其他数据源，然后动态地创建网页。

JSP标签有多种功能，比如访问数据库、记录用户选择信息、访问JavaBeans组件等，还可以在不同的网页中传递控制信息和共享信息。


------------

#### JSP 开发环境搭建 - 配置Java开发工具（JDK）
这一步涉及Java SDK的下载和PATH环境变量的配置。

您可以从Oracle公司的Java页面中下载SDK：[Java SE Downloads ](https://www.oracle.com/java/technologies/javase-downloads.html "Java SE Downloads ")

------------

#### JSP 开发环境搭建 - 设置Web服务器：Tomcat
目前，市场上有很多支持JSP和Servlets开发的Web服务器。他们中的一些可以免费下载和使用，Tomcat就是其中之一。

Apache Tomcat是一个开源软件，可作为独立的服务器来运行JSP和Servlets，也可以集成在 Apache Web Server中。

下载最新版本的Tomcat：[http://tomcat.apache.org/](http://tomcat.apache.org/ "http://tomcat.apache.org/")



