package com.lt.boot.aop;

import com.lt.boot.annotation.LogRecord;
import com.lt.boot.model.entity.SysLog;
import com.lt.boot.model.entity.User;
import com.lt.boot.service.SysLogService;
import com.lt.boot.service.UserService;
import com.lt.boot.utils.UserThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * 请求响应日志 AOP
 **/
@Aspect
@Component
@Slf4j
public class OperationLogAspect {
    @Resource
    private SysLogService sysLogService;
    @Resource
    private UserService userService;
    @Resource
    private Executor handleSysLogTaskExecutor;

    @Around("@annotation(logRecord)")
    public Object doInterceptor(ProceedingJoinPoint point, LogRecord logRecord) throws Throwable {
        // 计时
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // 获取请求路径
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        Signature signature = point.getSignature();
        // 生成请求唯一 id
        String requestId = UUID.randomUUID().toString();
        String url = httpServletRequest.getRequestURI();
        String ip = httpServletRequest.getRemoteHost();
        String operation = httpServletRequest.getMethod();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        String methodName = method.getName();
        // 获取请求参数
        Object[] args = point.getArgs();
        String reqParam = "[" + StringUtils.join(args, ", ") + "]";
        // 输出请求日志
        log.info("请求开始 id: {}, operation: {}, methodName: {}, url: {}, ip: {}, params: {}", requestId, operation, methodName, url, ip, reqParam);
        // 执行原方法
        Object result = point.proceed();
        // 输出响应日志
        stopWatch.stop();
        long totalTimeMillis = stopWatch.getTotalTimeMillis();
        log.info("请求结束 id: {}, costTime: {}ms", requestId, totalTimeMillis);
        String value = logRecord.value();
        // 当前登录用户
        Long userId = UserThreadLocalUtils.getUserId();
        // 异步保存日志
        CompletableFuture.runAsync(() -> {
            User user = userService.getById(userId);
            SysLog sysLog = new SysLog();
            sysLog.setUserId(userId);
            sysLog.setUsername(user == null ? "" : user.getUsername());
            sysLog.setValue(value);
            sysLog.setOperation(operation);
            sysLog.setCostTime(totalTimeMillis);
            sysLog.setUrl(url);
            sysLog.setMethodName(methodName);
            sysLog.setParams(reqParam);
            sysLog.setIp(ip);
            sysLogService.save(sysLog);
        }, handleSysLogTaskExecutor);
        return result;
    }
}

