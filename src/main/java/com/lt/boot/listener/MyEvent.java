package com.lt.boot.listener;

import com.lt.boot.model.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * @description: MyEvent 事件
 * @author: ~Teng~
 * @date: 2024/2/16 19:05
 */
@Getter
@Setter
public class MyEvent extends ApplicationEvent {
    private User user;

    public MyEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}
