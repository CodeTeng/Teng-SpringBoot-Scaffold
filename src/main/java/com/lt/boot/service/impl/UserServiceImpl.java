package com.lt.boot.service.impl;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.boot.common.ErrorCode;
import com.lt.boot.common.page.PageVO;
import com.lt.boot.constant.CommonConstant;
import com.lt.boot.constant.UserConstant;
import com.lt.boot.exception.BusinessException;
import com.lt.boot.exception.ThrowUtils;
import com.lt.boot.mapper.UserMapper;
import com.lt.boot.model.dto.user.*;
import com.lt.boot.model.entity.User;
import com.lt.boot.model.enums.user.UserGenderEnum;
import com.lt.boot.model.enums.user.UserRoleEnum;
import com.lt.boot.model.enums.user.UserStatusEnum;
import com.lt.boot.model.vo.user.UserVO;
import com.lt.boot.service.UserService;
import com.lt.boot.utils.CollUtils;
import com.lt.boot.utils.JwtUtils;
import com.lt.boot.utils.SqlUtils;
import com.lt.boot.utils.UserThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author teng
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2024-02-18 20:45:00
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public String userLogin(UserLoginDTO userLoginDTO) {
        // 1. 从 redis 中获取验证码信息
        String redisCode = redisTemplate.opsForValue().get(CommonConstant.CAPTCHA_PREFIX + userLoginDTO.getSessionId());
        if (StringUtils.isBlank(redisCode) || !redisCode.equalsIgnoreCase(userLoginDTO.getCode())) {
            log.info("图片验证码错误，正确验证码：{}，用户输入验证码：{}", redisCode, userLoginDTO.getCode());
            throw new BusinessException(ErrorCode.INVALID_VERIFY_CODE);
        }
        String userPassword = userLoginDTO.getUserPassword();
        userPassword = DigestUtils.md5DigestAsHex((UserConstant.SALT + userPassword).getBytes(StandardCharsets.UTF_8));
        // 2. 查询用户是否存在
        String username = userLoginDTO.getUsername();
        User user = lambdaQuery().eq(User::getUsername, username).eq(User::getUserPassword, userPassword).one();
        if (user == null) {
            log.info("用户登录失败，用户名或密码错误");
            throw new BusinessException(ErrorCode.PASSWORD_ERROR);
        }
        String userRole = user.getUserRole();
        String[] userRoleNames = StringUtils.split(userRole, ",");
        if (user.getStatus() == UserStatusEnum.DISABLED || ArrayUtils.contains(userRoleNames, UserRoleEnum.BAN.getValue())) {
            throw new BusinessException(ErrorCode.ACCOUNT_LOCKOUT);
        }
        // 3. 生成 token
        String token = JwtUtils.getToken(user.getId());
        // 4. 删除验证码
        redisTemplate.delete(CommonConstant.CAPTCHA_PREFIX + userLoginDTO.getSessionId());
        return token;
    }

    @Override
    public void userRegister(UserRegisterDTO userRegisterDTO) {
        String username = userRegisterDTO.getUsername();
        String userPassword = userRegisterDTO.getUserPassword();
        synchronized (username.intern()) {
            // 1. 账户不能重复
            Long count = lambdaQuery().eq(User::getUsername, username).count();
            if (count > 0) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "该用户已存在，请更改用户名");
            }
            // 2. 加密
            userPassword = DigestUtils.md5DigestAsHex((UserConstant.SALT + userPassword).getBytes(StandardCharsets.UTF_8));
            // 3. 插入数据
            User user = new User();
            user.setUsername(username);
            user.setUserPassword(userPassword);
            user.setUserAvatar(UserConstant.DEFAULT_USER_AVATAR);
            boolean result = save(user);
            ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "注册失败，数据库错误");
        }
    }

    @Override
    public UserVO getCurrentUser() {
        Long userId = UserThreadLocalUtils.getUserId();
        if (userId == null) {
            return new UserVO();
        }
        User user = getById(userId);
        if (user == null) {
            return new UserVO();
        }
        if (user.getStatus() == UserStatusEnum.DISABLED || user.getUserRole().contains(UserRoleEnum.BAN.getValue())) {
            throw new BusinessException(ErrorCode.ACCOUNT_LOCKOUT);
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public void addUser(UserAddDTO userAddDTO) {
        // 添加用户
        String username = userAddDTO.getUsername();
        // 1. 账户不能重复
        Long count = lambdaQuery().eq(User::getUsername, username).count();
        if (count > 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "账号重复，请修改账号名称");
        }
        String userRole = userAddDTO.getUserRole();
        String[] userRoleNames = StringUtils.split(userRole, ",");
        for (String userRoleName : userRoleNames) {
            if (!UserConstant.USER_ROLE_LIST.contains(userRoleName)) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "角色名称不合法");
            }
        }
        User user = new User();
        BeanUtils.copyProperties(userAddDTO, user);
        // 设置默认头像和密码
        user.setUserAvatar(UserConstant.DEFAULT_USER_AVATAR);
        String userPassword = DigestUtils.md5DigestAsHex((UserConstant.SALT + UserConstant.DEFAULT_USER_PASSWORD).getBytes(StandardCharsets.UTF_8));
        user.setUserPassword(userPassword);
        boolean result = save(user);
        if (!result) throw new BusinessException(ErrorCode.DB_SAVE_EXCEPTION);
    }

    @Override
    public void updateUser(UserUpdateDTO userUpdateDTO) {
        // 1. 账户不能重复
        Long count = lambdaQuery()
                .ne(User::getId, userUpdateDTO.getId())
                .eq(User::getUsername, userUpdateDTO.getUsername())
                .count();
        if (count > 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "账号重复，请修改账号名称");
        }
        String userRole = userUpdateDTO.getUserRole();
        String[] userRoleNames = StringUtils.split(userRole, ",");
        for (String userRoleName : userRoleNames) {
            if (!UserConstant.USER_ROLE_LIST.contains(userRoleName)) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "角色名称不合法");
            }
        }
        User user = new User();
        BeanUtils.copyProperties(userUpdateDTO, user);
        boolean result = updateById(user);
        if (!result) throw new BusinessException(ErrorCode.DB_UPDATE_EXCEPTION);
    }

    @Override
    public PageVO<User> listUserByPage(UserQuery userQuery) {
        Long id = userQuery.getId();
        String username = userQuery.getUsername();
        String userRole = userQuery.getUserRole();
        String[] roleNames = StringUtils.split(userRole, ",");
        String userPhone = userQuery.getUserPhone();
        String userRealName = userQuery.getUserRealName();
        UserGenderEnum userGender = userQuery.getUserGender();
        Integer userAge = userQuery.getUserAge();
        String userEmail = userQuery.getUserEmail();
        String unionId = userQuery.getUnionId();
        String mpOpenId = userQuery.getMpOpenId();
        String userProfile = userQuery.getUserProfile();
        UserStatusEnum status = userQuery.getStatus();
        String sortBy = userQuery.getSortBy();
        Boolean isAsc = userQuery.getIsAsc();
        Page<User> page = lambdaQuery()
                .eq(id != null, User::getId, id)
                .eq(userGender != null, User::getUserGender, userGender)
                .eq(userAge != null, User::getUserAge, userAge)
                .eq(StringUtils.isNotBlank(unionId), User::getUnionId, unionId)
                .eq(StringUtils.isNotBlank(mpOpenId), User::getMpOpenId, mpOpenId)
                .eq(status != null, User::getStatus, status)
                .in(ArrayUtils.isNotEmpty(roleNames), User::getUserRole, roleNames)
                .like(StringUtils.isNotBlank(username), User::getUsername, username)
                .like(StringUtils.isNotBlank(userPhone), User::getUserPhone, userPhone)
                .like(StringUtils.isNotBlank(userRealName), User::getUserRealName, userRealName)
                .like(StringUtils.isNotBlank(userEmail), User::getUserEmail, userEmail)
                .like(StringUtils.isNotBlank(userProfile), User::getUserProfile, userProfile)
                .page(userQuery.toMpPageDefaultSortByCreateTimeDesc());
        if (SqlUtils.validSortField(sortBy)) {
            page.addOrder(new OrderItem().setColumn(sortBy).setAsc(isAsc));
        }
        List<User> userList = page.getRecords();
        if (CollUtils.isEmpty(userList)) {
            return PageVO.empty(page);
        }
        return PageVO.of(page, userList);
    }

    @Override
    public PageVO<UserVO> listUserVOByPage(UserVOQuery userVOQuery) {
        Long id = userVOQuery.getId();
        String username = userVOQuery.getUsername();
        String userPhone = userVOQuery.getUserPhone();
        String userRealName = userVOQuery.getUserRealName();
        UserGenderEnum userGender = userVOQuery.getUserGender();
        Integer userAge = userVOQuery.getUserAge();
        String userEmail = userVOQuery.getUserEmail();
        String userProfile = userVOQuery.getUserProfile();
        Boolean isAsc = userVOQuery.getIsAsc();
        String sortBy = userVOQuery.getSortBy();
        Page<User> page = lambdaQuery()
                .eq(id != null, User::getId, id)
                .eq(userGender != null, User::getUserGender, userGender)
                .eq(userAge != null, User::getUserAge, userAge)
                .like(StringUtils.isNotBlank(username), User::getUsername, username)
                .like(StringUtils.isNotBlank(userPhone), User::getUserPhone, userPhone)
                .like(StringUtils.isNotBlank(userRealName), User::getUserRealName, userRealName)
                .like(StringUtils.isNotBlank(userEmail), User::getUserEmail, userEmail)
                .like(StringUtils.isNotBlank(userProfile), User::getUserProfile, userProfile)
                .page(userVOQuery.toMpPageDefaultSortByCreateTimeDesc());
        if (SqlUtils.validSortField(sortBy)) {
            page.addOrder(new OrderItem().setColumn(sortBy).setAsc(isAsc));
        }
        List<User> userList = page.getRecords();
        if (CollUtils.isEmpty(userList)) {
            return PageVO.empty(page);
        }
        List<UserVO> userVOList = userList.stream().map(user -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            return userVO;
        }).toList();
        return PageVO.of(page, userVOList);
    }

    @Override
    public String userLoginByMpOpen(WxOAuth2UserInfo userInfo) {
        String unionId = userInfo.getUnionId();
        String mpOpenId = userInfo.getOpenid();
        // 单机锁
        synchronized (unionId.intern()) {
            // 查询用户是否已存在
            User user = lambdaQuery().eq(User::getUnionId, unionId).one();
            // 被封号，禁止登录
            if (user != null && (UserStatusEnum.DISABLED == user.getStatus() || UserRoleEnum.BAN.getValue().equals(user.getUserRole()))) {
                throw new BusinessException(ErrorCode.ACCOUNT_LOCKOUT);
            }
            // 用户不存在则创建
            if (user == null) {
                user = new User();
                user.setUnionId(unionId);
                user.setMpOpenId(mpOpenId);
                user.setUserAvatar(userInfo.getHeadImgUrl());
                user.setUsername(userInfo.getNickname());
                boolean result = save(user);
                if (!result) {
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "登录失败");
                }
            }
            // 生成token
            return JwtUtils.getToken(user.getId());
        }
    }

    @Override
    public void updateMyUser(UserUpdateMyDTO userUpdateMyDTO) {
        Long userId = UserThreadLocalUtils.getUserId();
        if (userId == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        Long count = lambdaQuery()
                .ne(User::getId, userId)
                .eq(User::getUsername, userUpdateMyDTO.getUsername())
                .count();
        if (count > 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "账号重复，请修改账号名称");
        }
        User user = new User();
        BeanUtils.copyProperties(userUpdateMyDTO, user);
        user.setId(userId);
        boolean result = updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.DB_UPDATE_EXCEPTION);
    }

    @Override
    public void updateUserPwd(Long id, UserUpdatePwdDTO userUpdateDTO) {
        User user = new User();
        user.setId(id);
        String userPassword = DigestUtils.md5DigestAsHex((UserConstant.SALT + userUpdateDTO.getUserPassword()).getBytes(StandardCharsets.UTF_8));
        user.setUserPassword(userPassword);
        boolean result = updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
    }

    @Override
    public void updateUserMyPwd(UserUpdateMyPwdDTO userUpdateMyPwdDTO) {
        Long userId = UserThreadLocalUtils.getUserId();
        String oldPassword = userUpdateMyPwdDTO.getOldPassword();
        String userPassword = userUpdateMyPwdDTO.getUserPassword();
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_EXISTS);
        }
        String oldEntryPwd = DigestUtils.md5DigestAsHex((UserConstant.SALT + oldPassword).getBytes(StandardCharsets.UTF_8));
        if (!oldEntryPwd.equals(user.getUserPassword())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "用户原始密码错误");
        }
        userPassword = DigestUtils.md5DigestAsHex((UserConstant.SALT + userPassword).getBytes(StandardCharsets.UTF_8));
        User newUser = new User();
        newUser.setId(userId);
        newUser.setUserPassword(userPassword);
        boolean result = updateById(newUser);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
    }
}
