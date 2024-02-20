package com.lt.boot.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @description: 使用HttpSessionListener统计在线用户数的监听器
 * @author: ~Teng~
 * @date: 2024/2/16 18:44
 */
@Component
@Slf4j
public class MyHttpSessionListener implements HttpSessionListener {
    /**
     * 记录在线的用户数量
     */
    public Integer count = 0;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        log.info("新用户上线了");
        count++;
        se.getSession().getServletContext().setAttribute("count", count);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        log.info("用户下线了");
        count--;
        se.getSession().getServletContext().setAttribute("count", count);
    }
}
