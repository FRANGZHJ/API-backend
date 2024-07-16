package com.frank.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.frank.springbootinit.common.ErrorCode;
import com.frank.springbootinit.constant.CommonConstant;
import com.frank.springbootinit.exception.BusinessException;
import com.frank.springbootinit.mapper.UserInterfaceInfoMapper;
import com.frank.springbootinit.model.dto.userInterfaceInfo.UserInterfaceInfoQueryRequest;
import com.frank.springbootinit.model.entity.User;
import com.frank.springbootinit.model.entity.UserInterfaceInfo;
import com.frank.springbootinit.service.UserInterfaceInfoService;
import com.frank.springbootinit.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* @author Frank
* @description 针对表【user_interface_info(用户接口关系表)】的数据库操作Service实现
* @createDate 2024-07-16 13:17:45
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService {


    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean b) {
     Long userId = userInterfaceInfo.getUserId();
     Long interfaceId = userInterfaceInfo.getInterfaceId();
     Integer status = userInterfaceInfo.getStatus();
     Integer totalNum = userInterfaceInfo.getTotalNum();
     Integer leftNum = userInterfaceInfo.getLeftNum();

        if(userInterfaceInfo == null) throw new BusinessException(ErrorCode.PARAMS_ERROR);
        //如果是添加操作，所有参数必须为非空，
        if(b){
            if(userId == null || userId <=0 ) throw new BusinessException(ErrorCode.PARAMS_ERROR);
            if(interfaceId == null || interfaceId <=0) throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
    }

    @Override
    public Wrapper<UserInterfaceInfo> getQueryWrapper(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest) {


        if (userInterfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = userInterfaceInfoQueryRequest.getId();
        Long userId = userInterfaceInfoQueryRequest.getUserId();
        Long interfaceId = userInterfaceInfoQueryRequest.getInterfaceId();
        String sortField = userInterfaceInfoQueryRequest.getSortField();
        String sortOrder = userInterfaceInfoQueryRequest.getSortOrder();
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(userId != null,  "userId", userId);
        queryWrapper.like(interfaceId != null , "interfaceId", interfaceId);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
}




