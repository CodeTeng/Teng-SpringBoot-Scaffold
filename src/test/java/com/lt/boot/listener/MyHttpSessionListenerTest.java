package com.lt.boot.listener;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/2/16 18:46
 */
@RestController
@RequestMapping("/listener")
class MyHttpSessionListenerTest {
    /**
     * 获取当前在线人数
     */
    @GetMapping("/total")
    public String getTotalUser(HttpServletRequest request, HttpServletResponse response) {
        // 把sessionId记录在浏览器中
        Cookie cookie = new Cookie("JSESSIONID", URLEncoder.encode(request.getSession().getId(), StandardCharsets.UTF_8));
        cookie.setPath("/");
        // 设置cookie有效期为2天，设置长一点
        cookie.setMaxAge(48 * 60 * 60);
        response.addCookie(cookie);
        Integer count = (Integer) request.getSession().getServletContext().getAttribute("count");
        return "当前在线人数：" + count;
    }
}