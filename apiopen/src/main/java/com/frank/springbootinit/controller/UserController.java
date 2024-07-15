package com.frank.springbootinit.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.frank.springbootinit.annotation.AuthCheck;
import com.frank.springbootinit.constant.RequestHeaderConstant;
import com.frank.springbootinit.constant.UserConstant;
import com.frank.springbootinit.model.dto.user.*;
import com.frank.springbootinit.model.enums.UserRoleEnum;
import com.frank.springbootinit.model.vo.LoginUserVO;
import com.frank.springbootinit.common.BaseResponse;
import com.frank.springbootinit.common.DeleteRequest;
import com.frank.springbootinit.common.ErrorCode;
import com.frank.springbootinit.common.ResultUtils;
import com.frank.springbootinit.exception.BusinessException;
import com.frank.springbootinit.exception.ThrowUtils;
import com.frank.springbootinit.model.entity.User;
import com.frank.springbootinit.model.vo.UserVO;
import com.frank.springbootinit.service.UserService;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import freemarker.ext.jython.JythonWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;


/**
 * 用户接口
 *
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     * @param userRegisterRequest 传递过来一个json，实体化为注册类
     * @return 返回注册成功
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        ThrowUtils.throwIf(userRegisterRequest == null,ErrorCode.PARAMS_ERROR,"请求参数为空");
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        ThrowUtils.throwIf(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword),ErrorCode.PARAMS_ERROR,"请求参数为空");
        long result  = userService.userRegister(userAccount,userPassword,checkPassword);
        return ResultUtils.success(result);
    }

    /**
     * 做登录，将用户存储到session中
     * @param userLoginRequest
     * @param httpServletRequest
     * @return 返回用户id
     */
    @PostMapping("/login")
    public BaseResponse<Long> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest httpServletRequest){
        ThrowUtils.throwIf(userLoginRequest == null,ErrorCode.PARAMS_ERROR,"请求参数为空");
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        ThrowUtils.throwIf(StringUtils.isAnyBlank(userAccount,userPassword),ErrorCode.PARAMS_ERROR,"账户或者密码为空");
        Long result = userService.userLogin(userAccount,userPassword, httpServletRequest);
        return ResultUtils.success(result);
    }

    /**
     * 将登录状态从session中移除
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest httpServletRequest){

        boolean result = userService.userLogout(httpServletRequest);

        return ResultUtils.success(result);
    }

    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request){

        LoginUserVO loginUserVO = userService.getLoginUser(request);

        return ResultUtils.success(loginUserVO);
    }

    /**
     * 管理员添加用户
     * @param userAddRequest
     * @return
     */
    @PostMapping("/addUser")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest){
        if(userAddRequest == null) throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求为空");
        User user = new User();
        BeanUtil.copyProperties(userAddRequest,user);
        //默认密码为123456
        String password = "123456";
        String entryptPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        user.setUserPassword(entryptPassword);
        boolean save = userService.save(user);
        if( !save ) throw new BusinessException(ErrorCode.OPERATION_ERROR,"添加失败");

        return ResultUtils.success(user.getId());
    }

    /**
     * 管理员删除用户 根据id
     */

    @PostMapping("/deleteUser")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUser(@RequestBody UserDeleteRequest userDeleteRequest){
        if(userDeleteRequest == null) throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求错误");
        boolean b = userService.removeById(userDeleteRequest.getId());
        return ResultUtils.success(b);
    }

    /**
     * 管理修改用户信息
     */
    @PostMapping("/updateUser")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest){
        if(userUpdateRequest == null) throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求错误");
        User user = new User();
        BeanUtil.copyProperties(userUpdateRequest,user);
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result,ErrorCode.OPERATION_ERROR,"修改失败");

        return ResultUtils.success(true);
    }

    /**
     * 查询所有用户，分页
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<User>> listUserByPage(@RequestBody UserQueryRequest userQueryRequest,
                                                   HttpServletRequest request) {
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(new Page<>(current, size),
                userService.getQueryWrapper(userQueryRequest));
        return ResultUtils.success(userPage);
    }
}
