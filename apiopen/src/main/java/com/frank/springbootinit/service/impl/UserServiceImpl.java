package com.frank.springbootinit.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.frank.springbootinit.common.ErrorCode;
import com.frank.springbootinit.constant.CommonConstant;
import com.frank.springbootinit.constant.UserConstant;
import com.frank.springbootinit.controller.UserController;
import com.frank.springbootinit.exception.BusinessException;
import com.frank.springbootinit.exception.ThrowUtils;
import com.frank.springbootinit.mapper.UserMapper;
import com.frank.springbootinit.model.dto.user.UserLoginRequest;
import com.frank.springbootinit.model.dto.user.UserQueryRequest;
import com.frank.springbootinit.model.entity.User;
import com.frank.springbootinit.model.vo.LoginUserVO;
import com.frank.springbootinit.service.UserService;

import com.frank.springbootinit.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.apache.xmlbeans.impl.xb.xsdschema.Attribute;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;

/**
* @author Frank
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2024-07-12 13:32:35
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    /**
     * 用户注册
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        ThrowUtils.throwIf(userAccount.length() < 4, ErrorCode.PARAMS_ERROR,"用户账户过短");
        ThrowUtils.throwIf(userPassword.length() < 8, ErrorCode.PARAMS_ERROR, "用户密码过短");
        ThrowUtils.throwIf(!userPassword.equals(checkPassword), ErrorCode.PARAMS_ERROR, "两次密码输入不一致");

        //此时就进行注册，为了使得账户唯一，要给账户进行加锁
        synchronized (userAccount.intern()){
            //先查询是否存在当前账户
            LambdaQueryWrapper<User> userQueryWrapper = new LambdaQueryWrapper<>();
            userQueryWrapper.eq(User::getUserAccount,userAccount);
            Long count = this.baseMapper.selectCount(userQueryWrapper);
            ThrowUtils.throwIf(count > 0, ErrorCode.PARAMS_ERROR, "该用户已经存在");
            //插入是要对密码进行加密
            String encryptPassword = DigestUtils.md5DigestAsHex(userPassword.getBytes());
            //生成ak和sk
            String accessKey = DigestUtil.md5Hex(userAccount + RandomUtil.randomNumbers(5));
            String secretKey = DigestUtil.md5Hex(userAccount + RandomUtil.randomNumbers(8));
            User user = new User();
            user.setUserAccount(userAccount);
            user.setUserPassword(encryptPassword);
            user.setAccessKey(accessKey);
            user.setSecretKey(secretKey);
            boolean saveResult = this.save(user);
            ThrowUtils.throwIf(!saveResult,ErrorCode.OPERATION_ERROR,"用户注册失败");
            //返回插入的id
            return user.getId();
        }

    }

    @Override
    public Long userLogin(String userAccount, String userPassword, HttpServletRequest httpServletRequest) {
        //首先要检验该用户存不存在或者密码是否错误
        String encryptPassword = DigestUtils.md5DigestAsHex(userPassword.getBytes());
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUserAccount,userAccount)
                .eq(User::getUserPassword,encryptPassword);
        User user = this.baseMapper.selectOne(userLambdaQueryWrapper);
        ThrowUtils.throwIf(user == null,ErrorCode.PARAMS_ERROR,"账户或者密码错误");

        //将其保存在session中，用于请求校验
        httpServletRequest.getSession().setAttribute(UserConstant.USER_LOGIN,user);

        return user.getId();

    }

    @Override
    public boolean userLogout(HttpServletRequest httpServletRequest) {
        //如果session中没有，那么已经注销
        if(httpServletRequest.getSession().getAttribute(UserConstant.USER_LOGIN) == null){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"未登录");
        }

        httpServletRequest.getSession().removeAttribute(UserConstant.USER_LOGIN);

        return true;
    }

    @Override
    public LoginUserVO getLoginUser(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(UserConstant.USER_LOGIN);
        if(user == null ){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"当前用户未登录");
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtil.copyProperties(user,loginUserVO);

        return loginUserVO;
    }
    @Override
    public User getUser(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(UserConstant.USER_LOGIN);
        if(user == null ){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"当前用户未登录");
        }

        return user;
    }
    @Override
    public Wrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {

        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = userQueryRequest.getId();
        String userName = userQueryRequest.getUserName();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(StringUtils.isNotBlank(userRole), "userRole", userRole);
        queryWrapper.like(StringUtils.isNotBlank(userName), "userName", userName);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;

    }

    @Override
    public boolean isAdmin(HttpServletRequest request) {
        String userRole = (String) request.getSession().getAttribute(UserConstant.USER_LOGIN);
        if(userRole != null && userRole.equals(UserConstant.ADMIN_ROLE)) return true;
        return false;
    }


}




