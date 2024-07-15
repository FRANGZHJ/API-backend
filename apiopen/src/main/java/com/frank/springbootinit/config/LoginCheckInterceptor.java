package com.frank.springbootinit.config;


import com.frank.springbootinit.constant.RequestHeaderConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import javax.print.attribute.standard.MediaSize;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * 拦截器登录校验
 */
@Component
@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {
    
    //注入用户登录redis
    @Resource(name = "userRedisTemplate")
    private StringRedisTemplate userRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //先得到此次请求的token
        log.info("拦截器执行了");
        String token = request.getHeader(RequestHeaderConstant.REQUEST_HEADER_TOKEN);
        if(token == null || StringUtils.isAnyBlank(token)){
            return false;
        }
        String userInfo = userRedisTemplate.opsForValue().get(token);
        if(userInfo == null || StringUtils.isAnyBlank(userInfo)){
            //如果value不存在，那么这个token也可以删掉了
            userRedisTemplate.delete(token);
            return false;
        }
        //每次登录后刷新时间
        userRedisTemplate.expire(token,1, TimeUnit.DAYS);
        return true;
    }
}
