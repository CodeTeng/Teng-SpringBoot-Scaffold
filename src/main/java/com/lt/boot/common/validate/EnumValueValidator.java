package com.lt.boot.common.validate;

import com.lt.boot.common.validate.annotation.EnumValid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/2/16 22:11
 */
@Slf4j
public class EnumValueValidator implements ConstraintValidator<EnumValid, Integer> {
    private int[] enums = null;

    @Override
    public void initialize(EnumValid enumValid) {
        this.enums = enumValid.enumeration();
        log.info("payload>>{}", ArrayUtils.toString(enumValid.payload()));
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        //没有配置枚举值不校验
        if (ArrayUtils.isEmpty(enums)) {
            return true;
        }
        for (int e : enums) {
            if (e == value) {
                return true;
            }
        }
        return false;
    }
}
