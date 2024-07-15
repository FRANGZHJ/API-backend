package com.frank.springbootinit.controller;


import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.frank.springbootinit.annotation.AuthCheck;
import com.frank.springbootinit.common.BaseResponse;
import com.frank.springbootinit.common.DeleteRequest;
import com.frank.springbootinit.common.ErrorCode;
import com.frank.springbootinit.common.ResultUtils;
import com.frank.springbootinit.constant.UserConstant;
import com.frank.springbootinit.exception.BusinessException;
import com.frank.springbootinit.exception.ThrowUtils;
import com.frank.springbootinit.model.dto.interfaceInfo.InterfaceInfoAddRequest;
import com.frank.springbootinit.model.dto.interfaceInfo.InterfaceInfoQueryRequest;
import com.frank.springbootinit.model.dto.interfaceInfo.InterfaceInfoUpdateRequest;
import com.frank.springbootinit.model.entity.InterfaceInfo;
import com.frank.springbootinit.model.entity.User;
import com.frank.springbootinit.model.vo.InterfaceInfoVO;
import com.frank.springbootinit.model.vo.LoginUserVO;
import com.frank.springbootinit.service.InterfaceInfoService;
import com.frank.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/interfaceInfo")
public class InterfaceInfoController {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建
     *
     * @param interfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request) {
        if (interfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoAddRequest, interfaceInfo);
        interfaceInfoService.validInterfaceInfo(interfaceInfo, true);
        LoginUserVO loginUser = userService.getLoginUser(request);
        interfaceInfo.setUserId(loginUser.getId());
        boolean result = interfaceInfoService.save(interfaceInfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newInterfaceInfoId = interfaceInfo.getId();
        return ResultUtils.success(newInterfaceInfoId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LoginUserVO user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        boolean b = interfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param interfaceInfoUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest) {
        if (interfaceInfoUpdateRequest == null || interfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoUpdateRequest, interfaceInfo);
        // 参数校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, false);
        long id = interfaceInfoUpdateRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<InterfaceInfo> getInterfaceInfoVoById(Long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(interfaceInfo);
    }

    /**
     * 分页获取列表（仅管理员）
     *
     * @param interfaceInfoQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoByPage(@RequestBody InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        long current = interfaceInfoQueryRequest.getCurrent();
        long size = interfaceInfoQueryRequest.getPageSize();
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size),
                interfaceInfoService.getQueryWrapper(interfaceInfoQueryRequest));
        return ResultUtils.success(interfaceInfoPage);
    }

    /**
     * 上线接口
     * @param id
     * @return
     */
    @GetMapping ("/online")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> onlineInterface(Long id){
        //先判断id是否为空
        if(id == null || id <=0) throw new BusinessException(ErrorCode.PARAMS_ERROR);
        //先查看数据库中有无这个id的接口
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        if(interfaceInfo == null) throw new BusinessException(ErrorCode.PARAMS_ERROR,"当前接口不存在");
        InterfaceInfo interfaceInfo1 = new InterfaceInfo();
        interfaceInfo1.setId(id);
        interfaceInfo1.setStatus(1);
        boolean result = interfaceInfoService.updateById(interfaceInfo1);
        if(!result) throw new BusinessException(ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(result);
    }

    /**
     * 下线接口
     * @param id
     * @return
     */
    @GetMapping ("/offline")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> offlineInterface(Long id){
        //先判断id是否为空
        if(id == null || id <=0) throw new BusinessException(ErrorCode.PARAMS_ERROR);
        //先查看数据库中有无这个id的接口
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        if(interfaceInfo == null) throw new BusinessException(ErrorCode.PARAMS_ERROR,"当前接口不存在");
        InterfaceInfo interfaceInfo1 = new InterfaceInfo();
        interfaceInfo1.setId(id);
        interfaceInfo1.setStatus(0);  //这个魔法值可以写成一个枚举
        boolean result = interfaceInfoService.updateById(interfaceInfo1);
        if(!result) throw new BusinessException(ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(result);
    }

}
