package com.frank.frankinterfacesdk.Client;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.frank.frankinterfacesdk.model.User;
import com.frank.frankinterfacesdk.utils.DigesterUtil;

import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
        long randomNum = RandomUtil.randomInt() & 0x7fffffff; // 去掉符号位
        long timeStamp = System.currentTimeMillis()/1000;
        long nonce = randomNum << 32 | timeStamp;
        String content = DigesterUtil.getDigest().digestHex(accessKey + '.' + secretKey + '.' + nonce);
        headersMap.put("accesskey",accessKey);
        headersMap.put("sign",content);
        headersMap.put("nonce", String.valueOf(nonce));
        headersMap.put("timeStamp",String.valueOf(timeStamp));
        return headersMap;
    }

    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        long timestamp = now.toEpochSecond(ZoneOffset.UTC);
        long nonce = RandomUtil.randomInt() & 0x7fffffff;
        long timeStamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        System.out.println(nonce);
        System.out.println(timeStamp);
        System.out.println(timestamp << 32 | timestamp);

    }

}
