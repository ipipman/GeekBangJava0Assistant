### Dubbo 设计笔记

### Dubbo URL 设计

Dubbo URL 和标准的 URL 是有一些差别，标准URL 更为严格

Dubbo URL 是 Dubbo 贯穿整个框架的元信息载体

```java
consumer://
provider://
dubbo://
```



### Dubbo @SPI 设计

- 兼容 JDK SPI ServicLoader - META-INF/services
- META-INF/dubbo/ - Dubbo 外部应用扩展
- META-INF/dubbo/internal/ - Dubbo 内部框架实现
- META-INF/dubbo/external/ - Dubbo 外部框架实现

场景：SPI 有多种实现来源，第一种是 Dubbo 内部，第二种是应用扩展（Dubbo 外部）

假设 Dubbo 内部能够覆盖外部的话，那么 Dubbo 内部负载均衡永远无法同名的



#### SPI 加载策略 - org.apache.dubbo.common.extension.LoadingStrategy

- org.apache.dubbo.common.extension.DubboInternaLoadingStrategy（第一优先内部框架实现）
- org.apache.dubbo.common.extension.DubboExternalLoadingStrategy（第二优先外部框架实现）
- org.apache.dubbo.common.extension.DubboLoadingStrategy（第三优先外部应用扩展实现）
- org.apache.dubbo.common.extension.ServicesLoadingStrategy（第三优先JDK SPI ServiceLoader的兼容实现）

> ServicesLoadingStrategy 覆盖-> DubboLoadingStrategy 覆盖-> DubboExternalLoadingStrategy 覆盖-> DubboInternaLoadingStrategy



### Dubbo 配置

#### API 编程模型

##### 服务提供者 -ServiceConfig

##### 服务消费者 -ReferenceConfig



#### Spring 配置方式 - XML 配置编程模型

##### 服务提供者 -  <<dubbo:service>>

> ServiceConfig 中的 Properties 均被 <<dubbo:service>> 属性（Attributes）来设置

##### 服务消费者 - <<dubbo:reference>>



#### Spring 注解驱动模型

##### 服务提供者 -  @DubboService

> @DubboService 所标注的 Class 注册为普通的 Spring Bean （类似于 @Service，应用服务），同时将@DubboService 转换为 ServiceBean （Spring Bean，Dubbo组件）

```java
@DubboService
public class DefaultDemoService implements DemoService {
  
}

ServiceBean<DemoService> //Spring Bean，何时暴露 DemoService 服务？
// Bean 生命周期，还是 IoC 容器生命周期
// 理想时机：应用启动完毕，服务消费端消费前
```

简言之，一个@DubboService Class将注册两个 Spring BeanDefinition:

- 目标服务 BeanDefinition
- ServiceBean BeanDefinition



##### 服务消费者 - @DubboReferenceService







#### 

