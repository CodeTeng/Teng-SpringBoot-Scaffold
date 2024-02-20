package com.lt.boot.annotation;

import java.lang.annotation.*;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/2/18 22:46
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogRecord {
    /**
     * @return 操作描述
     */
    String value() default "";
}
