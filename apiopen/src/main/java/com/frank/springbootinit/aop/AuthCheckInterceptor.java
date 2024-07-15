package com.frank.springbootinit.aop;


import com.frank.springbootinit.annotation.AuthCheck;
import com.frank.springbootinit.common.ErrorCode;
import com.frank.springbootinit.exception.BusinessException;
import com.frank.springbootinit.model.enums.UserRoleEnum;
import com.frank.springbootinit.model.vo.LoginUserVO;
import com.frank.springbootinit.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * aop做权限校验
 */
@Aspect
@Component
public class AuthCheckInterceptor {

    @Resource
    private UserService userService;


    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        //先获取以下当前登录用户的信息
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        LoginUserVO loginUser = userService.getLoginUser(request);
        //获得注解方法的值
        String PostMappingle = authCheck.mustRole();
        //如果当前注解的内容不在用户作用域内放行
        UserRoleEnum PostMappingleEnum = UserRoleEnum.getEnumByValue(PostMappingle);
        if(PostMappingleEnum == null){
            return joinPoint.proceed();
        }
        //获得当前登录用户的作用
        UserRoleEnum loginUserRoleEnum = UserRoleEnum.getEnumByValue(loginUser.getUserRole());
        if(loginUserRoleEnum == null){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        //当前用户被封号直接拒绝
        if(UserRoleEnum.BAN.equals(loginUserRoleEnum)){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR,"被封号");
        }

        //如果当前注解为admin，用户也必须为admin
        if(UserRoleEnum.ADMIN.equals(PostMappingleEnum)){
            if(!UserRoleEnum.ADMIN.equals(loginUserRoleEnum)){
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR,"无权限");
            }
        }
        return joinPoint.proceed();
    }
}
