package com.lt.boot.model.enums.user;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.lt.boot.common.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description: 用户性别枚举
 * @author: ~Teng~
 * @date: 2024/2/17 15:23
 */
@Getter
@AllArgsConstructor
public enum UserGenderEnum implements BaseEnum {
    MAN(0, "男性"),
    WOMAN(1, "女性"),
    ;
    @EnumValue
    @JsonValue
    private final int value;
    private final String desc;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static UserGenderEnum of(Integer value) {
        if (value == null) return null;
        for (UserGenderEnum userGenderEnum : values()) {
            if (userGenderEnum.value == value) {
                return userGenderEnum;
            }
        }
        return null;
    }
}
