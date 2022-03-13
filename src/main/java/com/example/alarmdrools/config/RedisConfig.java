package com.example.alarmdrools.config;

//import com.example.alarmdrools.repository.RedisProblemDTORepo;

import com.example.alarmdrools.service.ProblemService;
import com.example.alarmdrools.service.RedisProblemService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import javax.persistence.EntityManager;


@Configuration
//@EnableRedisRepositories(basePackages = "com.example.alarmdrools.repository")
@ComponentScan("com.example.alarmdrools")
@PropertySource("classpath:application.properties")
public class RedisConfig {
    @Bean
    LettuceConnectionFactory lettuceConnectionFactory() {
        return new LettuceConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(lettuceConnectionFactory());
        return template;
    }


    @Bean
    public RedisProblemService redisProblemService() {
        return new RedisProblemService(redisTemplate());
    }

}
