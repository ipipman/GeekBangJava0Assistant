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
这样一来，就避免了重复开发、开发周期、代码可靠性差等问题。但是，各公司的中间件不兼容的问题就出现了，用户无法将他们组装在一起为自己服务，于是“标准”就应运而生了。

J2EE就是基于JAVA技术的一系列标准。
J2EE是Java2企业版（Java 2Platform Enterprise Edition），核心是一组技术规范与指南，其中所包含的各个组件，服务架构和技术层次，都有共同的标准及规格，让各种依赖J2EE架构的不同平台之间，存在良好的兼容性。


------------

### J2EE的13种规范
[![https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/geekbang/1311614079083_.pic.jpg](https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/geekbang/1311614079083_.pic.jpg "https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/geekbang/1311614079083_.pic.jpg")](https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/geekbang/1311614079083_.pic.jpg "https://ipman-blog-1304583208.cos.ap-nanjing.myqcloud.com/geekbang/1311614079083_.pic.jpg")


#### 1. JDBC（Java Database Connectivity）
JDBC 是以统一方式访问数据库的API

它提供了独立于平台的数据库访问，也就是说，有了JDBC API，我们就不必为了访问Oracle数据库专门写一个程序，而为了访问Mysql数据库又单独写一个程序等等。
只需要用JDBC API 写一个程序就够了，它可以向相应数据库发送SQL 调用，JDBC是Java 应用程序与各种不同数据库之间进行对话的方法的机制，简单地说，它做了三件事：

> - 与数据库建立连接
> - 发送操作数据库的语句
> - 处理结果

------------

#### 2. JNDI（JavaName and Directory Interface）
JNDI是一组在Java应用中访问命名和目录服务的API

命名服务将名称和对象联系起来，我们即可用名称访问对象，JNDI 允许吧名称同Java 对象或资源关联起来，建立逻辑关联，而不必知道对象或资源的物理ID。
JNDI为开发人员提供了查找和访问各种命名和目录服务的通用，统一了接口。

利用JNDI的命名与服务功能可满足企业级API 对命名与服务的访问，诸如：EJB、JMS、JDBC等。

JNDI和JDBC类似，都是构建在抽象层上
因为它提供了标准的独立于系统的API，这些API构建在命名系统之上，这一层有助于将应用与实际数据源分离，因此不管访问的LDAP、RMI还是DNS，也就是说，JNDI独立于目录服务的具体实现，只要有目录的服务提供接口或驱动，就可以使用目录

------------

#### 3. EJB （Enterprise Java Bean）
J2EE将业务逻辑从客户端软件中抽取出来，分装在一个组建中。

这个组件运行在一个独立的服务上，客户端软件通过网络调用组件提供的服务以实现业务逻辑，而客户端软件功能只负责发送调用请求和显示处理结果。

在J2EE中，这个运行在一个独立的服务器上，并封装了业务逻辑的组建就是EJB 组件。其实就是把原来放到客户端实现的代码放到服务器端，并以来RMI 进行通信。

------------

#### 4. RMI （Remote Method Invoke）
是一组用户开发分布式应用程序的API

这一协议调用远程对象上的方法使用了序列化的方式，在客户端与服务端之间传递数据。使得原先在同一操作系统方法调用，变成了不同操作系统之间程序的方法调用。即RMI 机制实现了程序组件在不同操作系统之间通信。

RMI属于EJB 更底层的通信协议

RMI / JNI：RMI 可以利用标准Java本机方法接口与现有的和原有的系统相连接
RMI / JDBC：RMI 利用标准JDBC 包与现有的关系型数据库连接

RMI实现了与飞Java 语言的现有服务进行通信

------------

#### 5. Java IDL / CORBA （Common Object Request Broker Architecture）
Java 接口定义语言，公用对象请求代理程序体系结构

在Java IDL的支持下，开发人员可以将Java 和 CORBA 集成在一起。他们可以创建Java 对象并使之可在CORBA ORB中展开，或者他们呢还可以创建Java 类并作为和其它ORB 一起展开的CORBA 对象的客户。后一种方式提供了另外一种途径，通过它Java 可以被用于拉新的应用和旧的系统相集成

CORBA 是面向对象标准的第一步，有了这个标准，软件的实现与工作环境对用户和开发者不再重要，可以把精力更多地放在本地系统的实现与优化上

------------

#### 6. JSP （JAVA Server Pages）
JSP页面 = HTML+Java，其根本是一个简化的Servlet设计

服务器在页面被客户端请求后，对这些Java 代码进行处理，然后将执行结果连同连同原HTML代码，生成的新HTML页面返回给客户端浏览器

------------

#### 7. Java Servlet
Servlet 是一种小型的Java 程序，扩展了Web 服务器的功能，作为一种服务器的应用，当请求时开始执行。

Servlet 提供的功能大多和 JSP 类似，不过，JSP 通常是大多数的HTML 代码中潜入少量的Java 代码。而Servlet 全部由Java 写成并生成 HTML

------------

#### 8. XML
XML 是一个用来定义其它标记语言的语言，可用做数据共享。

XML 的发展和Java 是相互独立的。
不过Java 具有的相同目标就是跨平台。通过Java 和 XML结合，我们可以得到一个完全和平台无关的解决方案

------------

#### 9. JMS （Java Message Service）
它是一种与厂商无关的API，用来访问消息收发系统消息

它类似于JDBC，JDBC是可以用来访问不同关系数据的API，而JMS 则提供同样与厂商无关的访问消息收发服务的方法，这样就可以通过消息收发服务实现从一个JMS 客户机向另一个JMS 客户端机发送消息。

JMS 是Java 平台上有关面向消息中间件的技术规范

------------

#### 10. JTA （Java Transaction API）
定义了一种标准API，应用程序由此可以访问各种事务监控

它允许应用程序执行分布式事务处理，在两个或多个网络计算机资源上访问并且更新数据，JTA 和 JTS 为J2EE 平台提供了分布式服务。
JTA 事务比JDBC 事务更强大，一个JTA 事务可以有多个参与者，而一个JDBC 事务则被限定在一个单一的数据库连接

------------

#### 11. JTS （Java Transaction Service）
JTS 是 CORBA OTS 事务监控器的一个基本实现

JTS 指定了一个事务管理器的实现（Transaction Manager），这个管理器在一个高级别上支持JTA 规范，并且在一个低级别上实现了OMGOTS 规范的Java 映射。

一个JTS 事务管理器为应用服务器、资源管理器、独立的应用和通信资源管理器提供事务服务

------------

#### 12. JavaMail
用于访问邮件服务器的API，提供了一套邮件服务器的抽象类

------------

#### 13. JAF（Java Beans Activation Framework）
JAF 是一个专用的数据处理框架

它用于封装数据，并为应用程序提供访问和操作数据的接口，也就是说JAF 让Java 程序知道怎么对一个数据源进行查看、编辑、打印等

JavaMail利用JAF 来处理MIME 编码的邮件附件

------------



### （二）JSP
#### 什么是Java Server Pages?
JSP全称Java Server Pages，是一种动态网页开发技术。它使用JSP标签在HTML网页中插入Java代码。标签通常以<%开头以%>结束。

JSP是一种Java servlet，主要用于实现Java web应用程序的用户界面部分。网页开发者们通过结合HTML代码、XHTML代码、XML元素以及嵌入JSP操作和命令来编写JSP。

JSP通过网页表单获取用户输入数据、访问数据库及其他数据源，然后动态地创建网页。

JSP标签有多种功能，比如访问数据库、记录用户选择信息、访问JavaBeans组件等，还可以在不同的网页中传递控制信息和共享信息。


------------

#### JSP 开发环境搭建 - 配置Java开发工具（JDK）
这一步涉及Java SDK的下载和PATH环境变量的配置。

您可以从Oracle公司的Java页面中下载SDK：[Java SE Downloads ](https://www.oracle.com/java/technologies/javase-downloads.html "Java SE Downloads ")


