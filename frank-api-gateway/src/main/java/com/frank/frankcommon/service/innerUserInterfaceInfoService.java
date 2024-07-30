package com.frank.frankcommon.service;

/**
* @author Frank
* @description 针对表【user_interface_info(用户接口关系表)】的数据库操作Service
* @createDate 2024-07-16 13:17:45
*/
public interface innerUserInterfaceInfoService {

    /**
     * 调用接口统计
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId, long userId);


}
