### Spring 运维架构重构



### 配置抽象

#### Profiles

Spring 应用启动过程中激活配置内容，通过逻辑比较判断，哪部分组件是否能够在其指定 Profiles 下激活。



#### PropertySources

核心API

**PropertySource 和 @PropertySource**

PropertySource 类似于 MicroPorfile Config 中的 ConfigSource

@PropertySource

**PropertySources 和 @PropertySources**

<img src="/Users/huangyan110110114/Library/Application Support/typora-user-images/image-20210417201758236.png" alt="image-20210417201758236"  width="800" align="left" />



### Spring JMX

核心API

JMX Bean 暴露器 - org.springframework.jmx.export.MBeanExporter

<img src="https://tva1.sinaimg.cn/large/008eGmZEly1gpn1g4d410j31j20u04qp.jpg"  width="700" align="left" />



#### Spring Logging

Spring repcakages

- Apache Commons Logging
- CGLIB
- ASM
- AOP Alliance
- ...

减少版本冲突



Spring 熟悉的API

- Spring - context
- Spring - beans
- Spring - core



#### 相关技术

#### 通用Java 对象生命周期

-  实例化阶段
  - 编码方法： new XXX(...)
  - 反射方式： XXX.class.newInstance(...)
- 属性被填充（赋值）阶段
- 初始化阶段
  - @PostConstruct
  - Spring InitializatingBean
  - 自定义 init 方法



#### MicroProfile Config

配置源 ConfigSource

### JMX

Jolokia - REST bridges JMX



#### 四种 MBean

- 标准 MBean
  - MBean  类结尾的接口
  - 标记为 @MBean 的接口
- 动态 MBean - 没有固定接口结构的
- 开放 MBean - 没有特定类型的
- 模型 MBean

> 结论：所有 JMX 类型的 MBean 都是 DynamicMBean 的实现







