package com.lt.boot.common.validate;

import com.lt.boot.common.enums.BaseEnum;
import com.lt.boot.common.validate.annotation.EnumValid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @description: 枚举校验器校验逻辑
 * @author: ~Teng~
 * @date: 2024/2/16 22:10
 */
@Slf4j
public class EnumValidator implements ConstraintValidator<EnumValid, BaseEnum> {
    private int[] enums = null;

    @Override
    public void initialize(EnumValid enumValid) {
        this.enums = enumValid.enumeration();
        log.info("payload>>{}", ArrayUtils.toString(enumValid.payload()));
    }

    @Override
    public boolean isValid(BaseEnum em, ConstraintValidatorContext context) {
        if (em == null) {
            return true;
        }
        // 没有配置枚举值不校验
        if (ArrayUtils.isEmpty(enums)) {
            return true;
        }
        for (int e : enums) {
            if (e == em.getValue()) {
                return true;
            }
        }
        return false;
    }
}
