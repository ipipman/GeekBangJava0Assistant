package org.ipman.web.projects.user.validator.bean.validation;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by ipipman on 2021/3/17.
 *
 * @version V1.0
 * @Package org.ipman.web.projects.user.validator.bean.validation
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/3/17 8:59 下午
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneValidAnnotationValidator.class)
public @interface PhoneValid {

    String message() default "";



}
