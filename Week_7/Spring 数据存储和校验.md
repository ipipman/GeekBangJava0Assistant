### Spring 数据存储和数据校验

#### 知识回顾

#### JDBC

##### 核心API

- 数据源 - javax.sql.DataSource
  - 生成SQL连接对象 （会话管理器）java.sql.Connection
- 生成SQL连接对象（会话管理器）java.sql.Connection
  - 有状态 - 缓存部分查询等结果
  - 无状态 - 每次和数据直接交互
- SQL命令执行器（会话） java.sql.Statement
  - 普通 -  java.sql.Statement
    - DML - executeUpdate 或者 executeBatch
    - DDL - execute
  - 预编译 -  java.sql.PreparedStatement
  - 存储过程 - java.sql.CallableStatement
- SQL 查询结果集 - java.sql.ResultSet
  - getXXX() 方法，参数：通过 column index 或 name



#### JPA

##### 核心API

- 实体管理（会话） 工厂 - javax.jpa.EntryManagerFactory

- 实体管理器（会话）- javax.jpa.EntityManager

- 依赖注入注解 - javax.persistence.PersistenceUnit

  - javax.persistence.PostLoad
  - Javax.persistence.PostPersist

  ```java
  @PersistenceUnit
  private EntityManagerFactory entityManagerFactory;
  ```



#### Bean Validation

##### 核心API

- 校验器工厂 - javax.validation.ValidationFactory
- 校验器 - javax.validation.Validation



#### Spring JDBC

<img src="https://tva1.sinaimg.cn/large/008eGmZEly1gpkphxtxwqj31bi0u0e81.jpg"  width="600" align="left" />



- 主门面接口 - org.springframework.jdbc.core.JdbcTemplate
  - 并非模版模式的实现，而是门面模式，也实现了命令模式
  - 面向数据表行列和面向对象混合编程（手动）
  - 通过 Callback 接口无需显示地捕获 SQLException, 而是 DataAccessException
  - 通过操作方法屏蔽 DDL 和 DML 在 JDBC API 中的差异
  - 可以帮助我们管理 Connection 和 Statement 的关闭操作
- Connection
  - 回调接口 - org.springframework.jdbc.core.ConnectionCallback
- Statement
  - 回调函数 - org.springframework.jdbc.core.StatementCallback
- PreparedStatement
  - 创建器 -  org.springframework.jdbc.core.PerparedStatementCreator
  - 回调接口 - org.springframework.jdbc.core.PerparedStatementCallback
  - 数据设置器 -  org.springframework.jdbc.core.PerparedStatementSetter



#### Spring  JDBC Template

<img src="https://tva1.sinaimg.cn/large/008eGmZEly1gpkqbjeq7dj312q0u07uk.jpg" width="500" align="left" />



#### Spring ORM

<img src="https://tva1.sinaimg.cn/large/008eGmZEly1gpkqzpk3z5j31m20u0e81.jpg" width="600" align="left" />

Spring ORM - Hibernate

org.springframework.orm.hibernate5.LocalSessionFactoryBean

= Local（Spring 容器创建） + SessionFactory + Bean（FactoryBean）

- 实现 Hibernate SessionFactory FactoryBean
- 实现 Spring Ioc Aware 回调
  - ResourceLoaderAware - 注入 ResourceLoader （读取 ClassLoader 资源）
  - BeanFactoryAware - 注入 BeanFactory （依赖查找）
- Spring Bean 生命周期回调
  - InitializationgBean
  - DisposableBean



Spring ORM - Hibernate - DEMO

<img src="https://tva1.sinaimg.cn/large/008eGmZEly1gpkrgfhnmmj31ye0k8e51.jpg" width="700" align="left" />



#### Spring DATA - JPA

org.spring.orm.jpa.LocalEntityManagerFactoryBean

= Local (Spring 容器)  + EntityManagerFactory + Bean （FactoryBean）

- 实现 JPA EntitySessionFactory FactoryBean
- 实现 Spring Ioc Aware 回调
  - BeanClassLoaderAware - 注入 Bean 所用到 ClassLoader
  - BeanNameAware - 注入当前 Bean 名称
  - ResourceLoaderAware - 注入 ResourceLoader （读取 ClassLoader 资源）
  - BeanFactoryAware - 注入 BeanFactory （依赖查找）
- Spring Bean 生命周期回调
  - InitializationgBean
  - DisposableBean
- 实现 PersistenceExceptionTranslator - 将存储相关的 JPA RuntimeException 转换为 Spring JDBC DataAccessException

#####  

#### Spring Data Commons

Spring Data 允许开发人员定义数据仓储接口，可以继承  CurdRespository 等， 并且只要符合 Spring Data 规约，就能自动地操作数据库，比如：

```java
public interface UserRespository extends CurdRepository<User, Long>{
  	List<User> findByIdAndName(Long id, String name); // where id = ? and name = ?
  
  	List<User> findByIdAndNameAndPassword(Long id, String name, String password); // where id = ? and name = ? and password = ?
}
```

Spring Data 利用自定义接口形成动态代理

核心API

- 数据仓储标记接口 - org.springframework.data.repository.Repository<T, ID>
  - T  代表 Entiry 类型
  - ID 代表 ID 主键类型
- CURD 数据仓储接口 - org.springframework.data.repository.CurdRepository
- 非数据仓储 Bean 注解 - NoRepositoryBean
  - 用于区分 Repository Bean 接口和非 Repository Bean 接口



####  Spring Validator

<img src="https://tva1.sinaimg.cn/large/008eGmZEly1gpksw28l9gj31dv0u0b29.jpg" width="600" align="left" />



org.spring.orm.validation. bean.LocalValidatorFactoryBean

= Local (Spring 容器)  + ValidatorFactory +Bean （FactoryBean）

- 扩展 org.springframework.validation.beanvalidation.SpringVidatorAdapter
- 实现 Spring Ioc Aware 回调
  - ResourceLoaderAware - 注入 ApplicationContext （BeanFactory）
- Spring Bean 生命周期回调
  - InitializationgBean
  - DisposableBean
- 实现 Bean Validation 标准接口 - ValidatorFacotory