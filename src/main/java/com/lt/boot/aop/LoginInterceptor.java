package com.lt.boot.aop;

import com.lt.boot.common.ErrorCode;
import com.lt.boot.exception.BusinessException;
import com.lt.boot.utils.JwtUtils;
import com.lt.boot.utils.UserThreadLocalUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/1/27 16:00
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    private List<String> urls = Arrays.asList(
            "/api/users/login", "/api//login/wx_open",  "/api/users/list/page/vo", "/api/users/register",
            "/api/users/findPassword", "/api/druid/index.html", "/api/captcha",
            "/api/swagger-ui.html", "/api/swagger-resources", "/api/webjars", "/api/doc.html",
            "/api/v2"
    );

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }
        String requestURI = request.getRequestURI();
        if (urls.contains(requestURI)) {
            // 直接放行
            return true;
        }
        Method method = handlerMethod.getMethod();
        System.out.println(requestURI);
        String methodName = method.getName();
        log.info("====拦截到了方法：{}，在该方法执行之前执行====", methodName);
        String token = request.getHeader("Authorization");
        log.info("token 获取为：{}", token);
        if (StringUtils.isBlank(token)) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "请先登录");
        }
        // 获取 payload 信息
        Claims claimsBody = JwtUtils.getClaimsBody(token);
        if (claimsBody == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "请先登录");
        }
        // 判断是否过期
        int res = JwtUtils.verifyToken(claimsBody);
        if (res > 1) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "用户登录已过期，请重新登录");
        }
        Integer userId = (Integer) claimsBody.get("userId");
        log.info("token 解析成功，userId = {}", userId);
        UserThreadLocalUtils.setUserId(Long.valueOf(userId));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserThreadLocalUtils.clear();
    }
}
