package com.lt.boot.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/2/20 13:33
 */
@Data
@ApiModel("图片验证码VO")
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class CaptchaVO implements Serializable {
    @ApiModelProperty("图片验证码Base64")
    private String imageData;
    @ApiModelProperty("唯一id")
    private String sessionId;
}
