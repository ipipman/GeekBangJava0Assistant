package com.bottomlord.projects.user.sql;

import java.lang.annotation.*;

import static java.sql.Connection.TRANSACTION_READ_COMMITTED;

/**
 * @author ChenYue
 * @date 2021/3/9 22:05
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LocalTransactional {
    int PROPAGATION_REQUIRED = 0;

    int PROPAGATION_REQUIRED_NEW = 3;

    int PROPAGATION_NESTED = 6;

    /**
     * 事务传播类型
     * @return 事务传播类型
     */
    int propagation() default PROPAGATION_REQUIRED;

    /**
     * 事务隔离级别
     * @return 事务隔离级别
     * @see java.sql.Connection#TRANSACTION_READ_COMMITTED
     */
    int isolation() default TRANSACTION_READ_COMMITTED;
}
