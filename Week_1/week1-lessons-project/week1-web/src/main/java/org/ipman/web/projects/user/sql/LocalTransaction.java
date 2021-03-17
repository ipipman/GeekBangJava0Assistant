package org.ipman.web.projects.user.sql;

/**
 * Created by ipipman on 2021/3/17.
 *
 * @version V1.0
 * @Package org.ipman.web.projects.user.sql
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/3/17 4:45 下午
 */

import java.lang.annotation.*;
import java.sql.Connection;

import static java.sql.Connection.TRANSACTION_READ_COMMITTED;

/**
 * 本地事务
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LocalTransaction {

    int PROPAGATION_REQUIRED = 0;

    int PROPAGATION_REQUIRES_NEW = 3;

    int PROPAGATION_NESTED = 6;

    /**
     * 事务传播
     * @return
     */
    int propagation() default PROPAGATION_REQUIRED;

    /**
     * 事务隔离级别
     * @return
     * @see Connection#TRANSACTION_READ_COMMITTED
     */
    int isolation() default TRANSACTION_READ_COMMITTED;
}
