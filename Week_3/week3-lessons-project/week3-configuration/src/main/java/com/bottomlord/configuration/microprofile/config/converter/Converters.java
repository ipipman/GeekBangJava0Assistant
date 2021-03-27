package com.bottomlord.configuration.microprofile.config.converter;

import org.eclipse.microprofile.config.spi.Converter;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author ChenYue
 * @date 2021/3/20 11:58
 */
public class Converters {
    private ClassLoader classLoader;
    private boolean addedDiscoveredConverters;
    public static final int DEFAULT_PRIORITY = 100;
    private final Map<Class<?>, PriorityQueue<PrioritizedConverter<? extends Object>>> typedMap;

    public Converters() {
        this(Thread.currentThread().getContextClassLoader());
    }

    public Converters(ClassLoader classLoader) {
        this.classLoader = classLoader;
        this.typedMap = new HashMap<>();
    }

    public void addDiscoveredConverters() {
        if (addedDiscoveredConverters) {
            return;
        }

        addConverters(ServiceLoader.load(Converter.class, classLoader));
        addedDiscoveredConverters = true;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @SuppressWarnings({"rawtypes"})
    public void addConverters(Iterable<Converter> converters) {
        converters.forEach(this::addConverter);
    }

    public void addConverter(Converter<?> converter) {
        addConverter(converter, DEFAULT_PRIORITY);
    }

    public void addConverter(Converter<?> converter, int priority) {
        Class<?> convertType = resolveConverterType(converter);
        addConverter(converter, priority, convertType);
    }

    public void addConverter(Converter<?> converter, int priority, Class<?> converterType) {
        PriorityQueue<PrioritizedConverter<?>> priorityQueue = this.typedMap.computeIfAbsent(converterType, t -> new PriorityQueue<>());
        priorityQueue.offer(new PrioritizedConverter<>(converter, priority));
    }

    private Class<?> resolveConverterType(Converter<?> converter) {
        assertConverter(converter);
        Class<?> converterType = null;
        Class<?> converterClass = converter.getClass();
        while (converterClass != null) {
            converterType = resolveConverterType(converterClass);
            if (converterType != null) {
                break;
            }

            Type superType = converterClass.getGenericSuperclass();
            if (superType instanceof ParameterizedType) {
                converterType = resolveConverterType(superType);
            }

            if (converterType != null) {
                return converterType;
            }

            converterClass = converterClass.getSuperclass();
        }

        return converterType;
    }

    private void assertConverter(Converter<?> converter) {
        Class<?> clazz = converter.getClass();

        if (clazz.isInterface()) {
            throw new IllegalArgumentException("The implementation class of Converter must not be an interface!");
        }

        if (Modifier.isAbstract(clazz.getModifiers())) {
            throw new IllegalArgumentException("The implementation class of Converter must not be an abstract class!");
        }
    }

    private Class<?> resolveConverterType(Class<?> converterClass) {
        Class<?> converterType = null;

        for (Type type : converterClass.getGenericInterfaces()) {
            converterType = resolveConverterType(type);
            if (converterType != null) {
                break;
            }
        }

        return converterType;
    }

    private Class<?> resolveConverterType(Type type) {
        Class<?> converterType = null;
        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            if (pType.getRawType() instanceof Class) {
                Class<?> rawType = (Class<?>) pType.getRawType();
                if (Converter.class.isAssignableFrom(rawType)) {
                    Type[] arguments = pType.getActualTypeArguments();
                    if (arguments.length == 1 && arguments[0] instanceof Class) {
                        converterType = (Class<?>) arguments[0];
                    }
                }
            }
        }

        return converterType;
    }

    public void addConverters(Converter<?>... converters) {
        addConverters(Arrays.asList(converters));
    }


    public List<Converter> getConverters(Class<?> convertType) {
        PriorityQueue<PrioritizedConverter<?>> queue = typedMap.get(convertType);

        return queue == null || queue.isEmpty() ? Collections.emptyList() : new LinkedList<>(queue);
    }

    public Iterator<Converter<?>> iterator() {
        List<Converter<?>> allConverters = new LinkedList<>();
        for (PriorityQueue<PrioritizedConverter<?>> converters : typedMap.values()) {
            for (PrioritizedConverter<?> converter : converters) {
                allConverters.add(converter.getConverter());
            }
        }
        return allConverters.iterator();
    }
}
