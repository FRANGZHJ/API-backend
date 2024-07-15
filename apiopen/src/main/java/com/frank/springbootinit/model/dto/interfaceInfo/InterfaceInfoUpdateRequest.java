package com.frank.springbootinit.model.dto.interfaceInfo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class InterfaceInfoUpdateRequest implements Serializable {

    /**
     * id不能改，用于update的时候查询
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应体
     */
    private String responseHeader;

    /**
     * 接口状态（0-关闭，1-开启)
     */
    private Integer status;

    /**
     * 请求类型
     */
    private String method;



    private static final long serialVersionUID = 1L;
}
