package com.lt.boot.model.dto.log;

import com.lt.boot.common.page.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/2/19 20:21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("后台日志分页查询请求体")
public class SysLogQuery extends PageQuery implements Serializable {
    private static final long serialVersionUID = 1L;
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
    @ApiModelProperty("请求url")
    private String url;
    @ApiModelProperty("请求方法（控制层方法全限定名）")
    private String methodName;
    @ApiModelProperty("IP地址")
    private String ip;
}
