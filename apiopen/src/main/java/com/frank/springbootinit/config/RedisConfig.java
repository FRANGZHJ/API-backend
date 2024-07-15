package com.frank.springbootinit.config;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.db1.host}")
    private String db1RedisHost;

    @Value("${spring.redis.db1.port}")
    private int db1RedisPort;

    @Value("${spring.redis.db1.database}")
    private int  database1;
    @Value("${spring.redis.db2.host}")
    private String db2RedisHost;

    @Value("${spring.redis.db2.port}")
    private int db2RedisPort;
    @Value("${spring.redis.db2.database}")
    private int  database2;

    @Bean(name = "db1RedisConnectionFactory")
    @Primary
    public LettuceConnectionFactory db1RedisConnectionFactory() {
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(db1RedisHost, db1RedisPort);
        lettuceConnectionFactory.setDatabase(database1);
        return lettuceConnectionFactory;
    }
    @Bean(name = "db2RedisConnectionFactory")
    public LettuceConnectionFactory db2RedisConnectionFactory() {
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(db2RedisHost, db2RedisPort);
        lettuceConnectionFactory.setDatabase(database2);
        return lettuceConnectionFactory;
    }

    @Bean(name = "userRedisTemplate")
    public StringRedisTemplate userRedisTemplate(@Qualifier("db1RedisConnectionFactory") LettuceConnectionFactory lettuceConnectionFactory) {
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        // 可以根据需要配置序列化器、键和值的序列化方式
        return redisTemplate;
    }

    @Bean(name = "redisTemplate2")
    public StringRedisTemplate redisTemplate2(@Qualifier("db2RedisConnectionFactory")  LettuceConnectionFactory lettuceConnectionFactory) {
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        // 可以根据需要配置序列化器、键和值的序列化方式等
        return redisTemplate;
    }
}
