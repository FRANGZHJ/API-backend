package com.frank.frankapigateway;

import com.frank.frankcommon.entity.User;
import com.frank.frankcommon.service.innerUserInterfaceInfoService;
import com.frank.frankcommon.service.innerUserService;
import com.frank.frankinterfacesdk.utils.DigesterUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Frank
 * @version 1.0
 * @date 2024/7/23 14:54
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    private innerUserService inneruserService;

    @DubboReference
    private innerUserInterfaceInfoService inneruserInterfacneInfoService;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("custom global filter");
//        2. 请求日志
        ServerHttpRequest request = exchange.getRequest();
        log.info("请求唯一标识：" + request.getId());
        log.info("请求路径：" + request.getPath());
        log.info("请求方法：" + request.getMethod());
        log.info("请求参数: " + request.getQueryParams());
        log.info("请求来源地址：" + request.getRemoteAddress());
//       todo 3. 用户鉴权，判断ak，sk是否合法
        //获得各个响应头
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String sign = headers.getFirst("sign");
        String nonce = headers.getFirst("nonce");
        String timeStamp = headers.getFirst("timeStamp");
        //从数据库中查询sk
        User invokeUser = inneruserService.getInvokeUser(accessKey);
        String secretKey = invokeUser.getSecretKey();
        String content = DigesterUtil.getDigest().digestHex(accessKey + '.' + secretKey);
        ServerHttpResponse response = exchange.getResponse();
        if(!content.equals(sign)){
            return handleNoAuth(response);
        }
        //先查询一下该用户的leftNum是否还有
        //然后再扣除
        try{
            inneruserInterfacneInfoService.invokeCount(1L, invokeUser.getId());
        }catch (Exception e){
            return handleNoAuth(response);
        }


//        5. 响应日志
//        6. 调用成功，接口调用次数+1
//        7. 调用失败，返回一个错误码
        return handleResponse(exchange,chain, 1L,invokeUser.getId());
    }

    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain,long interfaceId, long userId){
        try{
            //获得原始的响应对象
            ServerHttpResponse originalResponse = exchange.getResponse();
            //获得数据缓冲工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            //获得响应状态码
            HttpStatus statusCode = originalResponse.getStatusCode();
            //判断状态码是否为200(但是此时应该还没有调用，应该拿不到响应码)
            if(statusCode == HttpStatus.OK){
                //创建一个装饰的响应的独享
                ServerHttpResponseDecorator serverHttpResponseDecorator = new ServerHttpResponseDecorator(originalResponse){
                    @Override
                    //重写方法，用户处理响应题的数据
                    //这个方法就是模拟接口调用之后，这个函数来处理结果
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        //判断响应体是否为Flux类型
                        if(body instanceof Flux){
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            //返回一个处理后的响应体
                            return super.writeWith(fluxBody.map(dataBuffer -> {
                                //读取响应体的内容转换为字节数组
                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                //释放掉内存
                                DataBufferUtils.release(dataBuffer);
                                StringBuilder sb2  = new StringBuilder(200);
                                sb2.append("<--- {} {} \n");
                                List<Object> rspArgs = new ArrayList<>();
                                rspArgs.add(originalResponse.getStatusCode());
                                String data = new String(content, StandardCharsets.UTF_8);
                                //todo 这里要进行调用接口次数 +1 操作

                                sb2.append(data);
                                log.info(sb2.toString(), rspArgs.toArray());
                                return bufferFactory.wrap(content);
                            }));
                        }else{
                            log.error("<---- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                //对于200的请求，将装饰后的对象传递给下一个过滤器链，并继续处理
                return chain.filter(exchange.mutate().response(serverHttpResponseDecorator).build());

            }
            //如果非200，就直接返回
            return chain.filter(exchange);
        } catch (Exception e){
            //处理异常情况
            log.error("gateway log exception. \n" + e);
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }

    private Mono<Void> handleNoAuth(ServerHttpResponse response){
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }
}
