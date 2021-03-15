package org.ipman.web.projects.user.orm.jpa;

import org.ipman.web.context.ComponentContext;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.metamodel.Metamodel;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by ipipman on 2021/3/15.
 *
 * @version V1.0
 * @Package org.ipman.web.projects.user.orm.jpa
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/3/15 9:55 上午
 */

/*
 * 委派实现，静态 AOP
 */
public class DelegatingEntityManager implements EntityManager {

    private EntityManager entityManager;

    private String persistenceUnitName;

    private String propertiesLocation;

    // Setter 方法会被 Tomcat JNDI 实现调用
    public void setPersistenceUnitName(String persistenceUnitName) {
        this.persistenceUnitName = persistenceUnitName;
    }

    // Setter 方法会被 Tomcat JNDI 实现调用
    public void setPropertiesLocation(String propertiesLocation) {
        this.propertiesLocation = propertiesLocation;
    }

    // 初始化 EntityManager
    @PostConstruct
    public void init() {
        EntityManagerFactory entityManagerFactory =
                Persistence.createEntityManagerFactory(persistenceUnitName, loadProperties(propertiesLocation));
        // 创建 EntityManager
        this.entityManager = entityManagerFactory.createEntityManager();
    }

    // 加载 jpa-datasource.properties 配置
    private Map loadProperties(String propertiesLocation) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL propertiesFileURL = classLoader.getResource(propertiesLocation);
        if (propertiesFileURL == null) {
            return null;
        }
        Properties properties = new Properties();
        try {
            properties.load(propertiesFileURL.openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 增加 JNDI 的引用处理
        ComponentContext componentContext = ComponentContext.getInstance();
        for (String propertyName : properties.stringPropertyNames()) {
            String propertyValue = properties.getProperty(propertyName);
            if (propertyValue.startsWith("@")) {
                String componentName = propertyValue.substring(1);
                // 在 JNDI 中获取 DataSource （jdbc/UserPlatformDB）
                Object component = componentContext.getComponent(componentName);
                properties.put(propertyName, component);
            }
        }
        return properties;
    }

    @Override
    public void persist(Object o) {
        entityManager.persist(o);
    }

    @Override
    public <T> T merge(T t) {
        return entityManager.merge(t);
    }

    @Override
    public void remove(Object o) {
        entityManager.remove(o);
    }

    @Override
    public <T> T find(Class<T> aClass, Object o) {
        return entityManager.find(aClass, o);
    }

    @Override
    public <T> T find(Class<T> aClass, Object o, Map<String, Object> map) {
        return entityManager.find(aClass, o, map);
    }

    @Override
    public <T> T find(Class<T> aClass, Object o, LockModeType lockModeType) {
        return entityManager.find(aClass, o, lockModeType);
    }

    @Override
    public <T> T find(Class<T> aClass, Object o, LockModeType lockModeType, Map<String, Object> map) {
        return entityManager.find(aClass, o, lockModeType, map);
    }

    @Override
    public <T> T getReference(Class<T> aClass, Object o) {
        return entityManager.getReference(aClass, o);
    }

    @Override
    public void flush() {
        entityManager.flush();
    }

    @Override
    public void setFlushMode(FlushModeType flushModeType) {
        entityManager.setFlushMode(flushModeType);
    }

    @Override
    public FlushModeType getFlushMode() {
        return entityManager.getFlushMode();
    }

    @Override
    public void lock(Object o, LockModeType lockModeType) {
        entityManager.lock(o, lockModeType);
    }

    @Override
    public void lock(Object o, LockModeType lockModeType, Map<String, Object> map) {
        entityManager.lock(o, lockModeType, map);
    }

    @Override
    public void refresh(Object o) {
        entityManager.refresh(o);
    }

    @Override
    public void refresh(Object o, Map<String, Object> map) {
        entityManager.refresh(o, map);
    }

    @Override
    public void refresh(Object o, LockModeType lockModeType) {
        entityManager.refresh(o, lockModeType);
    }

    @Override
    public void refresh(Object o, LockModeType lockModeType, Map<String, Object> map) {
        entityManager.refresh(o, lockModeType, map);
    }

    @Override
    public void clear() {
        entityManager.clear();
    }

    @Override
    public void detach(Object o) {
        entityManager.detach(o);
    }

    @Override
    public boolean contains(Object o) {
        return entityManager.contains(o);
    }

    @Override
    public LockModeType getLockMode(Object o) {
        return entityManager.getLockMode(o);
    }

    @Override
    public void setProperty(String s, Object o) {
        entityManager.setProperty(s, o);
    }

    @Override
    public Map<String, Object> getProperties() {
        return entityManager.getProperties();
    }

    @Override
    public Query createQuery(String s) {
        return entityManager.createQuery(s);
    }

    @Override
    public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
        return entityManager.createQuery(criteriaQuery);
    }

    @Override
    public Query createQuery(CriteriaUpdate criteriaUpdate) {
        return entityManager.createQuery(criteriaUpdate);
    }

    @Override
    public Query createQuery(CriteriaDelete criteriaDelete) {
        return entityManager.createQuery(criteriaDelete);
    }

    @Override
    public <T> TypedQuery<T> createQuery(String s, Class<T> aClass) {
        return entityManager.createQuery(s, aClass);
    }

    @Override
    public Query createNamedQuery(String s) {
        return entityManager.createNamedQuery(s);
    }

    @Override
    public <T> TypedQuery<T> createNamedQuery(String s, Class<T> aClass) {
        return entityManager.createNamedQuery(s, aClass);
    }

    @Override
    public Query createNativeQuery(String s) {
        return entityManager.createNativeQuery(s);
    }

    @Override
    public Query createNativeQuery(String s, Class aClass) {
        return entityManager.createNativeQuery(s, aClass);
    }

    @Override
    public Query createNativeQuery(String s, String s1) {
        return entityManager.createNativeQuery(s, s1);
    }

    @Override
    public StoredProcedureQuery createNamedStoredProcedureQuery(String s) {
        return entityManager.createNamedStoredProcedureQuery(s);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String s) {
        return entityManager.createStoredProcedureQuery(s);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String s, Class... classes) {
        return entityManager.createStoredProcedureQuery(s, classes);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String s, String... strings) {
        return entityManager.createStoredProcedureQuery(s, strings);
    }

    @Override
    public void joinTransaction() {
        entityManager.joinTransaction();
    }

    @Override
    public boolean isJoinedToTransaction() {
        return entityManager.isJoinedToTransaction();
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        return entityManager.unwrap(aClass);
    }

    @Override
    public Object getDelegate() {
        return entityManager.getDelegate();
    }

    @Override
    public void close() {
        entityManager.close();
    }

    @Override
    public boolean isOpen() {
        return entityManager.isOpen();
    }

    @Override
    public EntityTransaction getTransaction() {
        return entityManager.getTransaction();
    }

    @Override
    public EntityManagerFactory getEntityManagerFactory() {
        return entityManager.getEntityManagerFactory();
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return entityManager.getCriteriaBuilder();
    }

    @Override
    public Metamodel getMetamodel() {
        return entityManager.getMetamodel();
    }

    @Override
    public <T> EntityGraph<T> createEntityGraph(Class<T> aClass) {
        return entityManager.createEntityGraph(aClass);
    }

    @Override
    public EntityGraph<?> createEntityGraph(String s) {
        return entityManager.createEntityGraph(s);
    }

    @Override
    public EntityGraph<?> getEntityGraph(String s) {
        return entityManager.getEntityGraph(s);
    }

    @Override
    public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> aClass) {
        return entityManager.getEntityGraphs(aClass);
    }
}
