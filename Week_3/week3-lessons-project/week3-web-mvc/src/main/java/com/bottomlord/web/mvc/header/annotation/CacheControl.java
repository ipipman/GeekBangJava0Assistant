package com.bottomlord.web.mvc.header.annotation;

import java.lang.annotation.*;

/**
 * @author ChenYue
 * @date 2021/3/2 18:20
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheControl {
}
