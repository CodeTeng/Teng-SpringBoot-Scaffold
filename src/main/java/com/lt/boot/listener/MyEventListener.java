package com.lt.boot.listener;

import com.lt.boot.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @description: 自定义监听器，监听MyEvent事件
 * @author: ~Teng~
 * @date: 2024/2/16 19:06
 */
@Component
@Slf4j
public class MyEventListener implements ApplicationListener<MyEvent> {
    @Override
    public void onApplicationEvent(MyEvent event) {
        // 把事件中的信息获取到
        User user = event.getUser();
        // 处理事件，实际项目中可以通知别的微服务或者处理其他逻辑等等
        log.info("用户名:{}", user.getUsername());
        log.info("用户密码:{}", user.getUserPassword());
    }
}
