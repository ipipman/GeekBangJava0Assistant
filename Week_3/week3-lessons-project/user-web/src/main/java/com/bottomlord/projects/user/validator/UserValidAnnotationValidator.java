package com.bottomlord.projects.user.validator;

import com.bottomlord.projects.user.domain.User;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @author ChenYue
 * @date 2021/3/10 13:01
 */
public class UserValidAnnotationValidator implements ConstraintValidator<UserValid, User> {
    private int idMin;
    private boolean checkEmail;
    private boolean checkPhone;
    private int maxPasswordLen;
    private int minPasswordLen;

    private static final String EMAIL_REGEX = "^(.+)@(\\S+)$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private static final String PHONE_REGEX = "^((13[0-9])|(14[5,7,9])|(15[0-3,5-9])|(166)|(17[3,5,6,7,8])" +
            "|(18[0-9])|(19[8,9]))\\d{8}$";
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);

    @Override
    public void initialize(UserValid annotation) {
        this.idMin = annotation.idMin();
        this.checkEmail = annotation.checkEmail();
        this.checkPhone = annotation.checkPhone();
        this.maxPasswordLen = annotation.maxPasswordLen();
        this.minPasswordLen = annotation.minPasswordLen();
    }

    @Override
    public boolean isValid(User value, ConstraintValidatorContext context) {
        if (value.getId() != null && value.getId() < idMin) {
            context.buildConstraintViolationWithTemplate("id不能小于" + idMin).addConstraintViolation();
            return false;
        } else if (checkEmail) {
            if (value.getEmail() == null) {
                context.buildConstraintViolationWithTemplate("email不能为空").addConstraintViolation();
                return false;
            }

            if (!EMAIL_PATTERN.matcher(value.getEmail()).matches()) {
                context.buildConstraintViolationWithTemplate("email格式不正确").addConstraintViolation();
                return false;
            }
        } else if (checkPhone) {
            if (value.getPhoneNumber() == null) {
                context.buildConstraintViolationWithTemplate("电话不能为空").addConstraintViolation();
                return false;
            }

            if (!PHONE_PATTERN.matcher(value.getPhoneNumber()).matches()) {
                context.buildConstraintViolationWithTemplate("电话格式不正确").addConstraintViolation();
                return false;
            }
        } else if (value.getPassword() == null) {
            context.buildConstraintViolationWithTemplate("电话格式不正确").addConstraintViolation();
            return false;
        } else if (value.getPassword().length() < minPasswordLen) {
            context.buildConstraintViolationWithTemplate("密码长度不能短于" + minPasswordLen).addConstraintViolation();
            return false;
        } else if (value.getPassword().length() > maxPasswordLen) {
            context.buildConstraintViolationWithTemplate("密码长度不能超过" + maxPasswordLen).addConstraintViolation();
            return false;
        }

        return true;
    }
}
