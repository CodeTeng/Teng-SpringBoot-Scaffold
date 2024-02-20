package com.lt.boot.aop;

import com.lt.boot.annotation.AuthCheck;
import com.lt.boot.common.ErrorCode;
import com.lt.boot.exception.BusinessException;
import com.lt.boot.model.enums.user.UserRoleEnum;
import com.lt.boot.model.enums.user.UserStatusEnum;
import com.lt.boot.model.vo.user.UserVO;
import com.lt.boot.service.UserService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 权限校验 AOP
 */
@Aspect
@Component
public class AuthInterceptor {
    @Resource
    private UserService userService;

    /**
     * 执行拦截
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        String mustRole = authCheck.mustRole();
        // 当前登录用户
        UserVO currentUser = userService.getCurrentUser();
        // 必须有该权限才通过
        if (StringUtils.isNotBlank(mustRole)) {
            UserRoleEnum mustUserRoleEnum = UserRoleEnum.getEnumByValue(mustRole);
            if (mustUserRoleEnum == null) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
            }
            String userRole = currentUser.getUserRole();
            String[] userRoles = StringUtils.split(userRole, ",");
            // 如果被封号，直接拒绝
            if (ArrayUtils.contains(userRoles, UserRoleEnum.BAN.getValue()) || currentUser.getStatus() == UserStatusEnum.DISABLED) {
                throw new BusinessException(ErrorCode.ACCOUNT_LOCKOUT);
            }
            // 必须有管理员权限
            if (UserRoleEnum.ADMIN.equals(mustUserRoleEnum)) {
                if (!mustRole.equals(userRole)) {
                    throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
                }
            }
        }
        // 通过权限校验，放行
        return joinPoint.proceed();
    }
}

