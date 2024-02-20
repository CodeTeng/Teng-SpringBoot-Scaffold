package com.lt.boot.model.enums.user;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.lt.boot.common.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description: 用户状态枚举
 * @author: ~Teng~
 * @date: 2024/2/17 15:30
 */
@Getter
@AllArgsConstructor
public enum UserStatusEnum implements BaseEnum {
    DISABLED(0, "禁用"),
    NORMAL(1, "正常"),
    ;
    @EnumValue
    @JsonValue
    private final int value;
    private final String desc;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static UserStatusEnum of(Integer value) {
        if (value == null) return null;
        for (UserStatusEnum userStatusEnum : values()) {
            if (userStatusEnum.value == value) {
                return userStatusEnum;
            }
        }
        return null;
    }
}
