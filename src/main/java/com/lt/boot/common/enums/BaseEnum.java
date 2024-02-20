package com.lt.boot.common.enums;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/2/16 22:09
 */
public interface BaseEnum {
    int getValue();
    String getDesc();

    default boolean equalsValue(Integer value) {
        if (value == null) {
            return false;
        }
        return getValue() == value;
    }
}
