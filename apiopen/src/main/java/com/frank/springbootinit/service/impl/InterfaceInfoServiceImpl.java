package com.frank.springbootinit.service.impl;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.frank.springbootinit.common.ErrorCode;
import com.frank.springbootinit.constant.CommonConstant;
import com.frank.springbootinit.exception.BusinessException;
import com.frank.springbootinit.mapper.InterfaceInfoMapper;
import com.frank.springbootinit.model.dto.interfaceInfo.InterfaceInfoQueryRequest;
import com.frank.springbootinit.model.entity.InterfaceInfo;
import com.frank.springbootinit.model.entity.User;
import com.frank.springbootinit.service.InterfaceInfoService;
import com.frank.springbootinit.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* @author Frank
* @description 针对表【interface_info(接口信息)】的数据库操作Service实现
* @createDate 2024-07-13 13:14:19
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService {

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean b) {
     Long id = interfaceInfo.getId();
     String name = interfaceInfo.getName();
     String description = interfaceInfo.getDescription();
     String url = interfaceInfo.getUrl();
     String requestHeader = interfaceInfo.getRequestHeader();
     String responseHeader = interfaceInfo.getResponseHeader();
     Integer status = interfaceInfo.getStatus();
     String method = interfaceInfo.getMethod();
     Long userId = interfaceInfo.getUserId();
     Date createTime = interfaceInfo.getCreateTime();
     Date updateTime = interfaceInfo.getUpdateTime();
     Integer isDelete = interfaceInfo.getIsDelete();

        if(interfaceInfo == null) throw new BusinessException(ErrorCode.PARAMS_ERROR);
        //如果是添加操作，所有参数必须为非空，
        if(b){
            if(StringUtils.isAnyBlank(name)) throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
    }

    @Override
    public Wrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        Long id = interfaceInfoQueryRequest.getId();
        String name = interfaceInfoQueryRequest.getName();
        String description = interfaceInfoQueryRequest.getDescription();
        String url = interfaceInfoQueryRequest.getUrl();
        String requestHeader = interfaceInfoQueryRequest.getRequestHeader();
        String responseHeader = interfaceInfoQueryRequest.getResponseHeader();
        Integer status = interfaceInfoQueryRequest.getStatus();
        String method = interfaceInfoQueryRequest.getMethod();
        Long userId = interfaceInfoQueryRequest.getUserId();
        int current = interfaceInfoQueryRequest.getCurrent();
        int pageSize = interfaceInfoQueryRequest.getPageSize();
        String sortField = interfaceInfoQueryRequest.getSortField();
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();

        if (interfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }

        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
}




