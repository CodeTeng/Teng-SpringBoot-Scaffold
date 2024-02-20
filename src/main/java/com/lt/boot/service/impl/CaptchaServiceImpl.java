package com.lt.boot.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.lt.boot.constant.CommonConstant;
import com.lt.boot.model.vo.CaptchaVO;
import com.lt.boot.service.CaptchaService;
import com.lt.boot.utils.IdWorkerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.awt.*;
import java.util.concurrent.TimeUnit;

/**
 * @description: 验证码业务类
 * @author: ~Teng~
 * @date: 2024/2/20 13:28
 */
@Service
@Slf4j
public class CaptchaServiceImpl implements CaptchaService {
    @Resource
    private IdWorkerUtils idWorkerUtils;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public CaptchaVO getCaptchaCode() {
        // 参数分别是宽、高、验证码长度、干扰线数量
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(200, 100, 4, 5);
        // 设置背景颜色清灰
        captcha.setBackground(Color.lightGray);
        //获取图片中的验证码，默认生成的校验码包含文字和数字，长度为4
        String checkCode = captcha.getCode();
        log.info("生成登录验证码:{}", checkCode);
        // 生成sessionId
        String sessionId = String.valueOf(idWorkerUtils.nextId());
        // 将sessionId和校验码保存在redis下，并设置缓存中数据存活时间一分钟
        redisTemplate.opsForValue().set(CommonConstant.CAPTCHA_PREFIX + sessionId, checkCode, 1, TimeUnit.MINUTES);
        // 组装响应数据 获取base64格式的图片数据
        return new CaptchaVO(captcha.getImageBase64(), sessionId);
    }
}
