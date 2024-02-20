package com.lt.boot.config;

import com.lt.boot.utils.IdWorkerUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/2/20 13:25
 */
@Configuration
public class CommonConfig {
    /**
     * 配置id生成器bean
     */
    @Bean
    public IdWorkerUtils idWorkerUtils() {
        // 基于运维人员对机房和机器的编号规划自行约定
        return new IdWorkerUtils(1, 21);
    }
}
