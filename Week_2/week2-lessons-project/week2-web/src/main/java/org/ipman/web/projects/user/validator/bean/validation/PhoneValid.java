package org.ipman.web.projects.user.validator.bean.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


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

    String message() default "{org.ipman.web.projects.user.validator.bean.validation.PhoneValid.message}";

    String pattern() default "^((13[0-9])|(14[0,1,4-9])|(15[0-3,5-9])|(16[2,5,6,7])|(17[0-8])|(18[0-9])|(19[0-3,5-9]))\\d{8}$";

    Class<?>[] groups() default {};

    Class<? extends Payload> [] payload() default {};
}
