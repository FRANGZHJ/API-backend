package com.frank.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.frank.springbootinit.model.dto.interfaceInfo.InterfaceInfoQueryRequest;
import com.frank.springbootinit.model.entity.InterfaceInfo;

/**
* @author Frank
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2024-07-13 13:14:19
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean b);

    Wrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest);

}
