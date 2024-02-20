package com.lt.boot.controller;

import com.lt.boot.common.BaseResponse;
import com.lt.boot.common.ResultUtils;
import com.lt.boot.model.vo.CaptchaVO;
import com.lt.boot.service.CaptchaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/2/20 13:35
 */
@Api(tags = "验证码模块")
@RestController
@RequestMapping("/captcha")
public class CaptchaController {
    @Resource
    private CaptchaService captchaService;

    @GetMapping
    @ApiOperation("生成图片验证码")
    public BaseResponse<CaptchaVO> getCaptchaCode() {
        CaptchaVO captchaVO = captchaService.getCaptchaCode();
        return ResultUtils.success(captchaVO);
    }
}
