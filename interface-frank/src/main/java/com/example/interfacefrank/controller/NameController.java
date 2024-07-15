package com.example.interfacefrank.controller;


import com.frank.frankinterfacesdk.model.User;
import com.frank.frankinterfacesdk.utils.DigesterUtil;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class NameController {

    /**
     * get，通过？拼接参数获得
     * @param name
     * @return
     */
    @GetMapping("/")
    public String getNameByGet(String name){
        return "我的名字是：" + name;
    }

    /**
     * restful获得参数
     */
    @PostMapping("/")
    public String getNameByPost(@RequestParam String name){
        return "我的名字是：" + name;
    }

    @PostMapping("/name")
    public String getNameByPost(@RequestBody User user, HttpServletRequest request){
        //获得各个响应头
        String accessKey = request.getHeader("accessKey");
        String sign = request.getHeader("sign");
        String nonce = request.getHeader("nonce");
        String timeStamp = request.getHeader("timeStamp");
        //从数据库中查询sk
        String secertKey = "123bbb";
        String content = DigesterUtil.getDigest().digestHex(accessKey + '.' + secertKey);
        //具体要通过redis来实现
//        if(Long.parseLong(nonce) > 10000){
//            throw new RuntimeException("无限期");
//        }
        if(!content.equals(sign)){
            throw new RuntimeException("无权限");
        }
        return "我的名字是: " + user.getName();
    }
}
