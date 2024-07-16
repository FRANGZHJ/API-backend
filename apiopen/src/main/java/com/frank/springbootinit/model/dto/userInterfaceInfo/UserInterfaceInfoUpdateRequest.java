package com.frank.springbootinit.model.dto.userInterfaceInfo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class UserInterfaceInfoUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 接口状态（0-关闭，1-开启)
     */
    private Integer status;

    /**
     * 总调用次数
     */
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;





    private static final long serialVersionUID = 1L;
}
