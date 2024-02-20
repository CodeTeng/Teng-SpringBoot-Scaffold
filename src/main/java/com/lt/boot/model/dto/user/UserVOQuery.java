package com.lt.boot.model.dto.user;

import com.lt.boot.common.page.PageQuery;
import com.lt.boot.model.enums.user.UserGenderEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/2/19 14:16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("前台用户分页查询请求体")
public class UserVOQuery extends PageQuery implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("用户id")
    private Long id;
    @ApiModelProperty("用户名")
    private String username;
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
    @ApiModelProperty("用户简介")
    private String userProfile;
}
