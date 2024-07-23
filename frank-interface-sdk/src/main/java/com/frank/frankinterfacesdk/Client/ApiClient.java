package com.frank.frankinterfacesdk.Client;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.frank.frankinterfacesdk.model.User;
import com.frank.frankinterfacesdk.utils.DigesterUtil;

import java.util.HashMap;
import java.util.Map;

public class ApiClient {

    private String accessKey;
    private String secretKey;
    private static final String uri = "http://localhost:8200";
    public ApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String getNameByGet(String name){
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", "frank");
        String result = HttpUtil.get(uri + "/api/user/",paramMap);
        System.out.println(result);
        return result;
    }

    public String getNameByPost(String name){
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", "frank");

        String result = HttpUtil.post(uri +"/api/user/" ,paramMap);
        System.out.println(result);
        return result;
    }

    public String getNameByPost(User user){
        String json = JSONUtil.toJsonStr(user);
        HttpResponse httpResponse = HttpRequest.post(uri + "/api/user/name")
                .addHeaders(genHeaderMap())
                .body(json)
                .execute();
        System.out.println(httpResponse.getStatus());
        String body = httpResponse.body();
        System.out.println(body);
        return body;
    }

    private Map<String,String> genHeaderMap(){
        Map<String,String> headersMap = new HashMap<>();
        headersMap.put("accesskey",accessKey);
        String content = DigesterUtil.getDigest().digestHex(accessKey + '.' + secretKey);
        headersMap.put("sign",content);
        headersMap.put("nonce", RandomUtil.randomNumbers(100));
        headersMap.put("timeStamp",String.valueOf(System.currentTimeMillis()/1000));
        return headersMap;
    }

}
