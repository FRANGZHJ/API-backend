package com.frank.springbootinit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.annotation.Resource;

@SpringBootTest
public class test01 {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void test(){
        stringRedisTemplate.opsForValue().set("12","123");
        String s = stringRedisTemplate.opsForValue().get("12");
        System.out.println(s);
    }
}
