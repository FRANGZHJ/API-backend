package com.example.interfacefrank;


import com.frank.frankinterfacesdk.Client.ApiClient;
import com.frank.frankinterfacesdk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class test1 {
    @Resource
    private ApiClient apiClient;
    @Test
    public void test01(){
        
        String frnak = apiClient.getNameByGet("frnak");
        String frank = apiClient.getNameByPost("frank");
        User frank2 = new User();
        frank2.setName("frank123123");
        String frank1 = apiClient.getNameByPost(frank2);

    }
}
