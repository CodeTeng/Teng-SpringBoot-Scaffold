package com.lt.boot.utils;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/2/18 22:01
 */
public class UserThreadLocalUtils {
    private final static ThreadLocal<Long> userThreadLocal = new ThreadLocal<>();

    /**
     * 设置当前线程中的用户
     */
    public static void setUserId(Long userId) {
        userThreadLocal.set(userId);
    }

    /**
     * 获取线程中的用户
     */
    public static Long getUserId() {
        return userThreadLocal.get();
    }

    /**
     * 清空线程中的用户信息
     */
    public static void clear() {
        userThreadLocal.remove();
    }
}
