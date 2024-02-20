package com.lt.boot.service;

import com.lt.boot.common.page.PageVO;
import com.lt.boot.model.dto.user.*;
import com.lt.boot.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.boot.model.vo.user.UserVO;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;

/**
 * @author teng
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2024-02-18 20:45:00
 */
public interface UserService extends IService<User> {
    /**
     * 用户登录
     *
     * @param userLoginDTO "用户登录请求体
     * @return token
     */
    String userLogin(UserLoginDTO userLoginDTO);

    /**
     * 用户注册
     *
     * @param userRegisterDTO 用户注册请求体
     */
    void userRegister(UserRegisterDTO userRegisterDTO);

    /**
     * 获取当前登录用户
     *
     * @return 用户查询脱敏视图
     */
    UserVO getCurrentUser();

    /**
     * 添加用户(后台管理)
     *
     * @param userAddDTO 添加用户请求体
     */
    void addUser(UserAddDTO userAddDTO);

    /**
     * 修改用户(后台管理)
     *
     * @param userUpdateDTO 更新用户请求体
     */
    void updateUser(UserUpdateDTO userUpdateDTO);

    /**
     * 分页查询用户(后台管理)
     *
     * @param userQuery 后台用户分页查询请求体
     * @return 分页结果
     */
    PageVO<User> listUserByPage(UserQuery userQuery);

    /**
     * 前台分页查询用户
     *
     * @param userVOQuery 前台用户分页查询请求体
     * @return 分页结果
     */
    PageVO<UserVO> listUserVOByPage(UserVOQuery userVOQuery);

    /**
     * 用户登录（微信开放平台）
     *
     * @param userInfo 请求信息
     * @return token
     */
    String userLoginByMpOpen(WxOAuth2UserInfo userInfo);

    /**
     * 更新个人信息
     *
     * @param userUpdateMyDTO 用户更新个人信息请求体
     */
    void updateMyUser(UserUpdateMyDTO userUpdateMyDTO);

    /**
     * 修改密码(后台管理)
     *
     * @param id               用户id
     * @param userUpdatePwdDTO 修改密码请求体
     */
    void updateUserPwd(Long id, UserUpdatePwdDTO userUpdatePwdDTO);

    /**
     * 当前用户个人修改密码
     *
     * @param userUpdateMyPwdDTO 用户个人修改密码请求体
     */
    void updateUserMyPwd(UserUpdateMyPwdDTO userUpdateMyPwdDTO);
}
