package com.lt.boot.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统日志表
 *
 * @TableName sys_log
 */
@TableName(value = "sys_log")
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("系统日志实体")
public class SysLog implements Serializable {
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty("主键id")
    private Long id;
    @ApiModelProperty("用户id")
    private Long userId;
    @ApiModelProperty("用户账号")
    private String username;
    @ApiModelProperty("用户操作描述")
    private String value;
    @ApiModelProperty("用户操作:DELETE ADD GET UPDATE")
    private String operation;
    @ApiModelProperty("响应时间,单位毫秒")
    private Long costTime;
    @ApiModelProperty("请求url")
    private String url;
    @ApiModelProperty("请求方法（控制层方法全限定名）")
    private String methodName;
    @ApiModelProperty("请求参数")
    private String params;
    @ApiModelProperty("IP地址")
    private String ip;
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty("是否删除 0-未删除 1-已删除")
    private Integer isDelete;
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}