package com.lt.boot.model.dto.user;


import com.lt.boot.common.page.PageQuery;
import com.lt.boot.model.enums.user.UserGenderEnum;
import com.lt.boot.model.enums.user.UserStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("后台用户分页查询请求体")
public class UserQuery extends PageQuery implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("用户id")
    private Long id;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("用户角色 user/admin/ban")
    private String userRole;
    @ApiModelProperty("用户手机号")
    private String userPhone;
    @ApiModelProperty("用户真实姓名")
    private String userRealName;
    @ApiModelProperty("性别：0-男性，1-女性")
    private UserGenderEnum userGender;
    @ApiModelProperty("用户年龄")
    private Integer userAge;
    @ApiModelProperty("用户邮箱")
    private String userEmail;
    @ApiModelProperty("微信开放平台id")
    private String unionId;
    @ApiModelProperty("公众号openId")
    private String mpOpenId;
    @ApiModelProperty("用户简介")
    private String userProfile;
    @ApiModelProperty("账户状态：0-禁用 1-正常")
    private UserStatusEnum status;
}