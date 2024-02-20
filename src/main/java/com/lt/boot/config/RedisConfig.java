package com.lt.boot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/2/20 13:02
 */
@Configuration
public class RedisConfig {
    /**
     * 配置redisTemplate bean，自定义数据的序列化的方式
     *
     * @param redisConnectionFactory 连接redis的工厂，底层有场景依赖启动时，自动加载
     */
    @Bean
    public RedisTemplate redisTemplate(@Autowired RedisConnectionFactory redisConnectionFactory) {
        // 1.构建RedisTemplate模板对象
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        // 2.为不同的数据结构设置不同的序列化方案
        // 设置key序列化方式
        template.setKeySerializer(new StringRedisSerializer());
        // 设置value序列化方式
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        // 设置hash中field字段序列化方式
        template.setHashKeySerializer(new StringRedisSerializer());
        // 设置hash中value的序列化方式
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        // 5.初始化参数设置
        template.afterPropertiesSet();
        return template;
    }
}
