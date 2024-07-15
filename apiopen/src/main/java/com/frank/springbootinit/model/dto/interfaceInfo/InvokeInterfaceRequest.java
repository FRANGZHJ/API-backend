package com.frank.springbootinit.model.dto.interfaceInfo;

import lombok.Data;

import java.io.Serializable;


@Data
public class InvokeInterfaceRequest implements Serializable {

    /**
     * 接口id
     */
    private Long id;

    /**
     * 接口参数
     */
    private String requestParam;

    private static final long serialVersionUID = 1L;
}
