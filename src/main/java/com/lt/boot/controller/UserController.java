package com.lt.boot.controller;

import com.lt.boot.annotation.AuthCheck;
import com.lt.boot.annotation.LogRecord;
import com.lt.boot.common.BaseResponse;
import com.lt.boot.common.ErrorCode;
import com.lt.boot.common.ResultUtils;
import com.lt.boot.common.page.PageVO;
import com.lt.boot.config.WxOpenConfig;
import com.lt.boot.constant.UserConstant;
import com.lt.boot.exception.BusinessException;
import com.lt.boot.exception.ThrowUtils;
import com.lt.boot.model.dto.user.*;
import com.lt.boot.model.entity.User;
import com.lt.boot.model.enums.user.UserStatusEnum;
import com.lt.boot.model.vo.user.UserVO;
import com.lt.boot.service.UserService;
import com.lt.boot.utils.ExcelUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/2/17 20:51
 */
@Api(tags = "用户模块")
@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private WxOpenConfig wxOpenConfig;

    @PostMapping("/login")
    @ApiOperation("用户登录")
    @ApiImplicitParam(name = "userLoginDTO", value = "用户登录请求体", required = true, dataType = "UserLoginDTO", paramType = "body")
    public BaseResponse<String> userLogin(@RequestBody @Validated UserLoginDTO userLoginDTO) {
        ThrowUtils.throwIf(userLoginDTO == null, ErrorCode.PARAMS_ERROR);
        String token = userService.userLogin(userLoginDTO);
        return ResultUtils.success(token);
    }

    @GetMapping("/login/wx_open")
    @ApiOperation("用户登录（微信开放平台）")
    @ApiImplicitParam(name = "code", value = "请求code", required = true, dataType = "String", paramType = "query")
    public BaseResponse<String> userLoginByWxOpen(@RequestParam("code") String code) {
        WxOAuth2AccessToken accessToken;
        try {
            WxMpService wxService = wxOpenConfig.getWxMpService();
            accessToken = wxService.getOAuth2Service().getAccessToken(code);
            WxOAuth2UserInfo userInfo = wxService.getOAuth2Service().getUserInfo(accessToken, code);
            String unionId = userInfo.getUnionId();
            String mpOpenId = userInfo.getOpenid();
            if (StringUtils.isAnyBlank(unionId, mpOpenId)) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "登录失败，系统错误");
            }
            String token = userService.userLoginByMpOpen(userInfo);
            return ResultUtils.success(token);
        } catch (Exception e) {
            log.error("userLoginByWxOpen error", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "登录失败，系统错误");
        }
    }

    @PostMapping("/register")
    @ApiOperation("用户注册")
    @ApiImplicitParam(name = "userRegisterDTO", value = "用户注册请求体", required = true, dataType = "UserRegisterDTO", paramType = "body")
    public BaseResponse<Boolean> userRegister(@RequestBody @Validated UserRegisterDTO userRegisterDTO) {
        ThrowUtils.throwIf(userRegisterDTO == null, ErrorCode.PARAMS_ERROR);
        // 密码和校验密码相同
        if (!userRegisterDTO.getUserPassword().equals(userRegisterDTO.getCheckPassword())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        userService.userRegister(userRegisterDTO);
        return ResultUtils.success(true);
    }

    @GetMapping("/get/login")
    @ApiOperation("获取当前登录用户")
    public BaseResponse<UserVO> getLoginUser() {
        UserVO currentUser = userService.getCurrentUser();
        return ResultUtils.success(currentUser);
    }

    @PutMapping("/update/myPwd")
    @ApiOperation("当前用户个人修改密码")
    @ApiImplicitParam(name = "userUpdateMyPwdDTO", value = "用户个人修改密码请求体", required = true, dataType = "UserUpdateMyPwdDTO", paramType = "body")
    @LogRecord("当前用户个人修改密码")
    public BaseResponse<Boolean> updateUserMyPwd(@RequestBody @Validated UserUpdateMyPwdDTO userUpdateMyPwdDTO) {
        ThrowUtils.throwIf(userUpdateMyPwdDTO == null, ErrorCode.PARAMS_ERROR);
        if (!userUpdateMyPwdDTO.getUserPassword().equals(userUpdateMyPwdDTO.getCheckPassword())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        userService.updateUserMyPwd(userUpdateMyPwdDTO);
        return ResultUtils.success(true);
    }

    @PostMapping("/list/page/vo")
    @ApiOperation("前台分页查询用户 默认按照创建时间降序，还可以根据用户年龄，用户生日，更新时间排序")
    @ApiImplicitParam(name = "userVOQuery", value = "前台用户分页查询", required = true, dataType = "UserVOQuery", paramType = "body")
    public BaseResponse<PageVO<UserVO>> listUserVOByPage(@RequestBody @Validated UserVOQuery userVOQuery) {
        ThrowUtils.throwIf(userVOQuery == null, ErrorCode.PARAMS_ERROR);
        // 简单限制爬虫
        ThrowUtils.throwIf(userVOQuery.getPageSize() > 50, ErrorCode.OPERATION_ERROR);
        PageVO<UserVO> page = userService.listUserVOByPage(userVOQuery);
        return ResultUtils.success(page);
    }

    @GetMapping("/get/vo/{id}")
    @ApiOperation("前台根据id获取用户")
    @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "Long", paramType = "path")
    public BaseResponse<UserVO> getUserVOById(@PathVariable("id") Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        User user = userService.getById(id);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        if (user.getStatus() == UserStatusEnum.DISABLED) {
            throw new BusinessException(ErrorCode.ACCOUNT_LOCKOUT);
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return ResultUtils.success(userVO);
    }

    @PutMapping("/update/my")
    @ApiOperation("更新个人信息")
    @ApiImplicitParam(name = "userUpdateMyDTO", value = "用户更新个人信息请求体", required = true, dataType = "UserUpdateMyDTO", paramType = "body")
    @LogRecord("更新个人信息")
    public BaseResponse<Boolean> updateMyUser(@RequestBody @Validated UserUpdateMyDTO userUpdateMyDTO) {
        ThrowUtils.throwIf(userUpdateMyDTO == null, ErrorCode.PARAMS_ERROR);
        userService.updateMyUser(userUpdateMyDTO);
        return ResultUtils.success(true);
    }

    @PostMapping("/add")
    @ApiOperation("添加用户(后台管理)")
    @ApiImplicitParam(name = "userAddDTO", value = "添加用户请求体 默认密码12345678", required = true, paramType = "body", dataType = "UserAddDTO")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @LogRecord("添加用户(后台管理)")
    public BaseResponse<Boolean> addUser(@RequestBody @Validated UserAddDTO userAddDTO) {
        ThrowUtils.throwIf(userAddDTO == null, ErrorCode.PARAMS_ERROR);
        userService.addUser(userAddDTO);
        return ResultUtils.success(true, "你已成功添加用户，默认密码为：" + UserConstant.DEFAULT_USER_PASSWORD);
    }

    @DeleteMapping("/delete/{ids}")
    @ApiOperation("删除用户(单个和批量)(后台管理)")
    @ApiImplicitParam(name = "ids", value = "用户ids 批量删除时用,隔开", required = true, paramType = "path", dataType = "String")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @LogRecord("删除用户(单个和批量)(后台管理)")
    public BaseResponse<Boolean> deleteUser(@PathVariable("ids") String ids) {
        ThrowUtils.throwIf(StringUtils.isBlank(ids), ErrorCode.PARAMS_ERROR);
        String[] strings = StringUtils.split(ids, ",");
        ThrowUtils.throwIf(ArrayUtils.isEmpty(strings), ErrorCode.PARAMS_ERROR);
        List<Long> idList = Arrays.stream(strings).map(Long::parseLong).toList();
        boolean result = userService.removeBatchByIds(idList);
        ThrowUtils.throwIf(!result, ErrorCode.DB_DELETE_EXCEPTION);
        return ResultUtils.success(true);
    }

    @PutMapping("/update")
    @ApiOperation("修改用户(后台管理)")
    @ApiImplicitParam(name = "userUpdateDTO", value = "后台更新用户请求体", required = true, dataType = "UserUpdateDTO", paramType = "body")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @LogRecord("修改用户(后台管理)")
    public BaseResponse<Boolean> updateUser(@RequestBody @Validated UserUpdateDTO userUpdateDTO) {
        ThrowUtils.throwIf(userUpdateDTO == null, ErrorCode.PARAMS_ERROR);
        userService.updateUser(userUpdateDTO);
        return ResultUtils.success(true);
    }

    @PutMapping("/update/pwd/{id}")
    @ApiOperation("修改密码(后台管理)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userUpdateDTO", value = "后台修改密码请求体", required = true, dataType = "UserUpdatePwdDTO", paramType = "body"),
            @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "Long", paramType = "path")
    })
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @LogRecord("修改密码(后台管理)")
    public BaseResponse<Boolean> updateUserPwd(@PathVariable("id") Long id, @RequestBody @Validated UserUpdatePwdDTO userUpdatePwdDTO) {
        ThrowUtils.throwIf(userUpdatePwdDTO == null || id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        if (!userUpdatePwdDTO.getUserPassword().equals(userUpdatePwdDTO.getCheckPassword())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        userService.updateUserPwd(id, userUpdatePwdDTO);
        return ResultUtils.success(true);
    }

    @PostMapping("/list/page")
    @ApiOperation("分页查询用户(后台管理)默认按照创建时间降序，还可以根据用户年龄，用户生日，更新时间排序")
    @ApiImplicitParam(name = "userQuery", value = "后台用户分页查询", required = true, dataType = "UserQuery", paramType = "body")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<PageVO<User>> listUserByPage(@RequestBody @Validated UserQuery userQuery) {
        ThrowUtils.throwIf(userQuery == null, ErrorCode.PARAMS_ERROR);
        PageVO<User> page = userService.listUserByPage(userQuery);
        return ResultUtils.success(page);
    }

    @GetMapping("/get/{id}")
    @ApiOperation("根据id获取用户(后台管理)")
    @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "Long", paramType = "path")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<User> getUserById(@PathVariable("id") Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
    }

    @PostMapping("/export/page")
    @ApiOperation("用户导出 根据条件进行导出")
    @ApiImplicitParam(name = "userQuery", value = "后台用户分页查询", required = true, dataType = "UserQuery", paramType = "body")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @LogRecord("用户数据导出(后台管理)")
    public BaseResponse<Boolean> exportUser(@RequestBody @Validated UserQuery userQuery, HttpServletResponse response) throws Exception {
        ThrowUtils.throwIf(userQuery == null, ErrorCode.PARAMS_ERROR);
        PageVO<User> page = userService.listUserByPage(userQuery);
        List<User> list = page.getList();
        ExcelUtils.doExport(list, User.class, "用户数据", response);
        return ResultUtils.success(true);
    }
}
