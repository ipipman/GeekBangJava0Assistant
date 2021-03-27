package com.bottomlord.projects.user.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ChenYue
 * @date 2021/3/10 12:59
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UserValidAnnotationValidator.class})
public @interface UserValid {
    /**
     * @return id最小值
     */
    int idMin() default 1;

    /**
     * @return 是否检查电话
     */
    boolean checkPhone() default true;

    /**
     * @return 是否检查邮件
     */
    boolean checkEmail() default true;

    /**
     * @return 最短密码长度
     */
    int minPasswordLen() default 6;

    /**
     * @return 最长密码长度
     */
    int maxPasswordLen() default 20;

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


}
