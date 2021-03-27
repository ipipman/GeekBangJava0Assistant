package com.bottomlord.configuration.microprofile.config.converter;

import org.eclipse.microprofile.config.spi.Converter;

/**
 * @author ChenYue
 * @date 2021/3/20 12:54
 */
public class PrioritizedConverter<T> implements Converter<T>, Comparable<PrioritizedConverter<T>>{
    private final Converter<T> converter;
    private final int priority;

    public PrioritizedConverter(Converter<T> converter, int priority) {
        this.converter = converter;
        this.priority = priority;
    }

    @Override
    public int compareTo(PrioritizedConverter<T> other) {
        return Integer.compare(other.priority, this.priority);
    }

    @Override
    public T convert(String value) throws IllegalArgumentException, NullPointerException {
        return this.converter.convert(value);
    }

    public Converter<T> getConverter() {
        return this.converter;
    }

    public int getPriority() {
        return this.priority;
    }
}
