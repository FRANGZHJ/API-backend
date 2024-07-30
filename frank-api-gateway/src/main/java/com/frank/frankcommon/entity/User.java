package com.frank.frankcommon.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Frank
 * @version 1.0
 * @date 2024/7/27 14:02
 */
@Data
public class User implements Serializable {
    /**
     * id，雪花生成
     */
    private Long id;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvator;

    /**
     * 用户角色：user/admin
     */
    private String userRole;

    private String accessKey;

    private String secretKey;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Integer isDelete;

    private static final long serialVersionUID = 1L;
}
