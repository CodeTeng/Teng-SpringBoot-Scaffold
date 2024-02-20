package com.lt.boot.listener;

import com.lt.boot.model.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/2/16 19:09
 */
@SpringBootTest
public class MyEventListenerTest {
    @Resource
    private ApplicationContext applicationContext;

    /**
     * 发布事件
     */
    @Test
    public void getUser() throws InterruptedException {
        User user = new User();
        user.setUsername("木子Teng");
        user.setUserPassword("123456");
        // 发布事件 异步执行
        MyEvent event = new MyEvent(this, user);
        applicationContext.publishEvent(event);
        Thread.sleep(5000L);
    }
}
