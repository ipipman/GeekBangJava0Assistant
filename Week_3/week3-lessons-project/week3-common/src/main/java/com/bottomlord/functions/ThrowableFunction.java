package com.bottomlord.functions;

/**
 * @author ChenYue
 * @date 2021/3/2 22:33
 */
@FunctionalInterface
public interface ThrowableFunction<IN, OUT> {
    /**
     * applies the function to the given arguments
     * @param in the function argument
     * @return result
     * @throws Throwable Throwable if met with any error
     */
    OUT apply(IN in) throws Throwable;

    /**
     * Execute {@link ThrowableFunction}
     * @param in the function argument
     * @return result
     * @throws RuntimeException runtime wrapppers {@link Throwable}
     */
    default OUT execute(IN in) throws RuntimeException {
        OUT result;
        try {
            result = apply(in);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * Execute {@link ThrowableFunction}
     * @param in the function argument
     * @param function funtion {@link ThrowableFunction}
     * @param <IN> the source type
     * @param <OUT> the return type
     * @return the result after execution
     */
    static <IN, OUT> OUT execute(IN in, ThrowableFunction<IN, OUT> function) {
        return function.execute(in);
    }
}
