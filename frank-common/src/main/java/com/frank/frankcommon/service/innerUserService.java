package com.frank.frankcommon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.frank.frankcommon.model.entity.User;

/**
* @author Frank
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2024-07-12 13:32:35
*/
public interface innerUserService  {


    /**
     * 数据库中是否已经分配密钥
     * @param accessKey
     * @return
     */
    User getInvokeUser(String accessKey);
}
