package com.frank.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.frank.springbootinit.model.dto.user.UserQueryRequest;
import com.frank.springbootinit.model.entity.User;
import com.frank.springbootinit.model.vo.LoginUserVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author Frank
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2024-07-12 13:32:35
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     * @param userAccount
     * @param userPassword
     * @param httpServletRequest
     * @return
     */
    Long userLogin(String userAccount, String userPassword, HttpServletRequest httpServletRequest);

    /**
     * 用户注销，移除session
     * @param httpServletRequest
     * @return
     */
    boolean userLogout(HttpServletRequest httpServletRequest);

    /**
     * 获取当前登录用户信息，从session中取到
     * @param request
     * @return
     */
    LoginUserVO getLoginUser(HttpServletRequest request);

    Wrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    boolean isAdmin(HttpServletRequest request);
}
