package com.lt.boot.service;

import com.lt.boot.model.vo.CaptchaVO;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/2/20 13:28
 */
public interface CaptchaService {
    /**
     * 生成图片验证码
     *
     * @return 图片验证码VO
     */
    CaptchaVO getCaptchaCode();
}
