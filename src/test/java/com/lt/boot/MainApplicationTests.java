package com.lt.boot;

import com.lt.boot.config.WxOpenConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

/**
 * 主类测试
 */
@SpringBootTest
class MainApplicationTests {

    @Resource
    private WxOpenConfig wxOpenConfig;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    void contextLoads() {
        System.out.println(wxOpenConfig);
        System.out.println(redisTemplate);
//        System.out.println(stringRedisTemplate);
    }

}
