package org.ipman.web.projects.user.validator.bean.validation;

import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by ipipman on 2021/3/17.
 *
 * @version V1.0
 * @Package org.ipman.web.projects.user.validator.bean.validation
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/3/17 9:02 下午
 */
public class PhoneValidAnnotationValidator implements ConstraintValidator<PhoneValid, String> {

    private Pattern CHINA_PATTERN ;

    @Override
    public void initialize(PhoneValid phoneValid) {
        this.CHINA_PATTERN = Pattern.compile(phoneValid.pattern());
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.isEmpty(s)) {
            return false;
        }
        return CHINA_PATTERN.matcher(s).matches();
    }
}
