### Spring 分布式缓存

 

### Spring Cache 抽象

#### 基本实现模式：

#### 缓存操作注解

##### @Cacheable

##### @CacheEvict

##### @CachePut



#### 缓存操作源数据 API - CacheOperation

##### @Cacheable - CacheableOperation

##### @CacheEvict - CacheEvictOperation

##### @CachePut - CachePutOperation 



#### 缓存操作元数据来源 - CacheOperationSource

注解实现 - AnnotationCacheOperationSource



#### 缓存注解解释器 - CacheAnnotationParser

具体实现 - SpringCacheAnnotationParser

```java
@Service
public class BookService{ // 被代理的 Bean
  
  @Cacheable(cacheNames="books", key="#isbn")
  // "boos" Cache, key = "#isbn" -> JCache - Cache<ISBN, Book>
  //													   -> Spring Cache
  public Booke findBook(ISBN isbn, boolean checkwarehouse, boolean includeUsed){
    
  }
  
  public Booke getBooke(...){ // CacheOperation 集合 == 空
    
  }
}
```



<img src="/Users/huangyan110110114/Library/Application Support/typora-user-images/image-20210429214400782.png" alt="image-20210429214400782"  align="left" width="500" />