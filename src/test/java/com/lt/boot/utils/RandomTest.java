package com.lt.boot.utils;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/1/27 16:42
 */
public class RandomTest {

    @Test
    public void testRandom() {
//        System.out.println(RandomUtil.randomNumbers(6));
        String ids = "1,2";
        System.out.println(Arrays.toString(ids.split(",")));
    }
}
