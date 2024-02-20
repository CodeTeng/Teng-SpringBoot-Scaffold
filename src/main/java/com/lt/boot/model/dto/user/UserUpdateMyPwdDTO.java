package com.lt.boot.model.dto.user;

import com.lt.boot.constant.RegexConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/2/19 18:29
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("用户个人修改密码请求体")
public class UserUpdateMyPwdDTO extends UserUpdatePwdDTO implements Serializable {
    @ApiModelProperty(value = "用户原始密码", required = true)
    @NotBlank(message = "用户密码不能为空")
    @Pattern(regexp = RegexConstants.PASSWORD_PATTERN, message = "密码为6~32位的字母、数字、下划线")
    private String oldPassword;
}
