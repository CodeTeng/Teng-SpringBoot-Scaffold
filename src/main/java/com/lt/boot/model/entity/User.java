package com.lt.boot.model.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.*;
import com.lt.boot.common.UserConvert;
import com.lt.boot.model.enums.user.UserGenderEnum;
import com.lt.boot.model.enums.user.UserStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户表
 *
 * @TableName user
 */
@TableName(value = "user")
@Data
@ApiModel("用户实体")
public class User implements Serializable {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键id")
    @ExcelProperty("用户id")
    private Long id;
    @ApiModelProperty("用户账号")
    @ExcelProperty("用户账号")
    private String username;
    @ApiModelProperty("用户密码")
    @ExcelIgnore
    private String userPassword;
    @ApiModelProperty("用户角色 user/admin/ban")
    @ExcelProperty("用户角色")
    private String userRole;
    @ApiModelProperty("用户手机号")
    @ExcelProperty("用户手机号")
    @ColumnWidth(11)
    private String userPhone;
    @ApiModelProperty("用户真实姓名")
    @ExcelProperty("用户真实姓名")
    private String userRealName;
    @ApiModelProperty("性别：0-男性，1-女性")
    @ExcelProperty(value = "用户性别", converter = UserConvert.UserGenderConvert.class)
    private UserGenderEnum userGender;
    @ApiModelProperty("用户年龄")
    @ExcelProperty("用户年龄")
    private Integer userAge;
    @ApiModelProperty("用户邮箱")
    @ExcelProperty("用户邮箱")
    @ColumnWidth(25)
    private String userEmail;
    @ApiModelProperty("用户头像")
    @ExcelIgnore
    private String userAvatar;
    @ApiModelProperty("用户生日")
    @ExcelProperty("用户生日")
    @DateTimeFormat("yyyy年MM月dd日")
    @ColumnWidth(20)
    private LocalDate userBirthday;
    @ApiModelProperty("微信开放平台id")
    @ExcelProperty("微信开放平台id")
    private String unionId;
    @ApiModelProperty("公众号openId")
    @ExcelProperty("公众号openId")
    private String mpOpenId;
    @ApiModelProperty("用户简介")
    @ExcelProperty("用户简介")
    private String userProfile;
    @ApiModelProperty("账户状态：0-禁用 1-正常")
    @ExcelProperty(value = "账户状态", converter = UserConvert.UserStatusConvert.class)
    private UserStatusEnum status;
    @ApiModelProperty("创建时间")
    @ExcelProperty("创建时间")
    @DateTimeFormat("yyyy年MM月dd日HH时mm分ss秒")
    @ColumnWidth(25)
    private LocalDateTime createTime;
    @ApiModelProperty("更新时间")
    @ExcelIgnore
    private LocalDateTime updateTime;
    @ApiModelProperty("创建者id")
    @ExcelIgnore
    @TableField(fill = FieldFill.INSERT)
    private Long creater;
    @ApiModelProperty("更新者id")
    @ExcelIgnore
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updater;
    @ApiModelProperty("是否删除 0-未删除 1-已删除")
    @ExcelIgnore
    private Integer isDelete;
    @TableField(exist = false)
    @ExcelIgnore
    private static final long serialVersionUID = 1L;
}