package com.frank.frankcommon.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * id，雪花生成
     */
    @TableId(value = "id", type =IdType.ASSIGN_ID)
    private Long id;

    /**
     * 账号
     */
    @TableField(value = "userAccount")
    private String userAccount;

    /**
     * 密码
     */
    @TableField(value = "userPassword")
    private String userPassword;

    /**
     * 用户名称
     */
    @TableField(value = "userName")
    private String userName;

    /**
     * 用户头像
     */
    @TableField(value = "userAvator")
    private String userAvator;

    /**
     * 用户角色：user/admin
     */
    @TableField(value = "userRole")
    private String userRole;

    @TableField(value = "accessKey")
    private String accessKey;

    @TableField(value = "secretKey")
    private String secretKey;
    /**
     * 创建时间
     */
    @TableField(value = "createTime")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "updateTime")
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableField(value = "isDelete")
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}