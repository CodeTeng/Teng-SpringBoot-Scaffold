package com.lt.boot.model.dto.user;

import com.lt.boot.constant.RegexConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 用户登录请求
 */
@Data
@ApiModel("用户登录请求体")
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserLoginDTO implements Serializable {
    private static final long serialVersionUID = 3191241716373120793L;
    @ApiModelProperty(value = "用户名", required = true)
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = RegexConstants.USERNAME_PATTERN, message = "用户名为4~32位的字母、数字、下划线")
    private String username;
    @ApiModelProperty(value = "用户密码", required = true)
    @NotBlank(message = "用户密码不能为空")
    @Pattern(regexp = RegexConstants.PASSWORD_PATTERN, message = "密码为6~32位的字母、数字、下划线")
    private String userPassword;
    @ApiModelProperty(value = "图片验证码", required = true)
    @NotBlank(message = "验证码不能为空")
    private String code;
    @ApiModelProperty(value = "保存redis随机码的key，也就是sessionId", required = true)
    @NotBlank(message = "sessionId不能为空")
    private String sessionId;
}
