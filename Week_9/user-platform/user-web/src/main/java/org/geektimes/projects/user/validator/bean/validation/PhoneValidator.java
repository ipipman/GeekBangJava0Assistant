package org.geektimes.projects.user.validator.bean.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * <p>
 * 自定义手机号码校验
 * </p>
 *
 * @author liuwei
 * @since 2021/3/9
 */
public class PhoneValidator implements ConstraintValidator<Phone, String> {

	private static final Pattern PATTERN = Pattern.compile("^1[3,4,5,6,7,8,9]\\d{9}$");

	@Override
	public void initialize(Phone parameters) { }

	@Override
	public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
		if (value == null){
			return true;
		}
		return PATTERN.matcher(value).matches();
	}

}
