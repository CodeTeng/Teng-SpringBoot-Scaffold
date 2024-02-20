package com.lt.boot.model.dto.user;

import com.lt.boot.common.validate.annotation.EnumValid;
import com.lt.boot.constant.RegexConstants;
import com.lt.boot.model.enums.user.UserGenderEnum;
import com.lt.boot.model.enums.user.UserStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@ApiModel("更新用户请求体")
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "用户id", required = true)
    @NotNull(message = "用户id不能为空")
    private Long id;
    @ApiModelProperty(value = "用户名", required = true)
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = RegexConstants.USERNAME_PATTERN, message = "用户名为4~32位的字母、数字、下划线")
    private String username;
    @ApiModelProperty(value = "用户角色集合", required = true)
    @NotBlank(message = "用户角色不能为空")
    private String userRole;
    @ApiModelProperty("用户手机号")
    @Pattern(regexp = RegexConstants.PHONE_PATTERN, message = "请填写正确的手机号")
    private String userPhone;
    @ApiModelProperty("用户真实姓名")
    private String userRealName;
    @ApiModelProperty(value = "性别：0-男性，1-女性", required = true)
    @EnumValid(enumeration = {0, 1}, message = "用户性别不符合")
    private UserGenderEnum userGender;
    @ApiModelProperty("用户年龄")
    @Min(value = 0L, message = "请填写正确的年龄")
    @Max(value = 120L, message = "请填写正确的年龄")
    private Integer userAge;
    @ApiModelProperty("用户邮箱")
    @Pattern(regexp = RegexConstants.EMAIL_PATTERN, message = "请填写正确的邮箱格式")
    private String userEmail;
    @ApiModelProperty("用户头像")
    private String userAvatar;
    @ApiModelProperty("用户生日")
    private LocalDate userBirthday;
    @ApiModelProperty("用户简介")
    private String userProfile;
    @ApiModelProperty(value = "账户状态：0-禁用 1-正常", required = true)
    @EnumValid(enumeration = {0, 1}, message = "用户状态不符合")
    private UserStatusEnum status;
}