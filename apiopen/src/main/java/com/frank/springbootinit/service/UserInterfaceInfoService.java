package com.frank.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.frank.springbootinit.model.dto.userInterfaceInfo.UserInterfaceInfoQueryRequest;
import com.frank.springbootinit.model.entity.UserInterfaceInfo;

/**
* @author Frank
* @description 针对表【user_interface_info(用户接口关系表)】的数据库操作Service
* @createDate 2024-07-16 13:17:45
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean b);

    Wrapper<UserInterfaceInfo> getQueryWrapper(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest);
}
