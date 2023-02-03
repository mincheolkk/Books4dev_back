package com.project.book.common.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.host}")
    private String host;

    @Bean
    public RedisConnectionFactory loginConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        config.setDatabase(0);
        return new LettuceConnectionFactory(config);
    }

    @Bean
    public RedisConnectionFactory rankingConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        config.setDatabase(1);
        return new LettuceConnectionFactory(config);
    }

    @Bean
    public RedisConnectionFactory keywordConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        config.setDatabase(2);
        return new LettuceConnectionFactory(config);
    }

    @Bean
    public RedisConnectionFactory rateLimiterConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        config.setDatabase(3);
        return new LettuceConnectionFactory(config);
    }

    @Bean
    @Qualifier("redisTemplateBean")
    public StringRedisTemplate redisTemplate(){
        StringRedisTemplate redisTemplate = new StringRedisTemplate(loginConnectionFactory());
        return redisTemplate;
    }

    @Bean
    @Qualifier("rankingRedisTemplateBean")
    public StringRedisTemplate rankingRedisTemplate() {
        StringRedisTemplate redisTemplate = new StringRedisTemplate(rankingConnectionFactory());
        return redisTemplate;
    }

    @Bean
    @Qualifier("keywordRedisTemplateBean")
    public RedisTemplate<Long, String> keywordRedisTemplate() {
        RedisTemplate<Long, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new GenericToStringSerializer<>(Long.class));
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(keywordConnectionFactory());
        return redisTemplate;
    }

    @Bean
    @Qualifier("rateLimiterRedisTemplateBean")
    public RedisTemplate<String, Integer> rateLimiterRedisTemplate() {
        RedisTemplate<String, Integer> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Integer.class));
        redisTemplate.setConnectionFactory(rateLimiterConnectionFactory());
        return redisTemplate;
    }


}
