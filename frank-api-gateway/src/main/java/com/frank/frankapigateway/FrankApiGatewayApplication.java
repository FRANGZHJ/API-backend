package com.frank.frankapigateway;

import com.frank.frankcommon.entity.User;
import com.frank.frankcommon.service.innerUserInterfaceInfoService;
import com.frank.frankcommon.service.innerUserService;
import com.frank.springbootinit.provider.DemoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

//@SpringBootApplication(exclude = {
//        DataSourceAutoConfiguration.class,
//        DataSourceTransactionManagerAutoConfiguration.class,
//        HibernateJpaAutoConfiguration.class})
@SpringBootApplication
@EnableDubbo
@Service
public class FrankApiGatewayApplication {

    @DubboReference
    private DemoService demoService;

    @DubboReference
    private innerUserService inneruserService;
    @DubboReference
    private innerUserInterfaceInfoService inneruserInterfaceInfoService;
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(FrankApiGatewayApplication.class, args);
        FrankApiGatewayApplication application = context.getBean(FrankApiGatewayApplication.class);
        String result = application.doSayHello("world");
        String result2 = application.doSayHello2("world");
        System.out.println("result: " + result);
        System.out.println("result: " + result2);
        User user = application.getUser("21f473238d972e5ff2b18b8e249098bc");
        System.out.println(user.getUserAccount());
    }

    public String doSayHello(String name) {
        return demoService.sayHello(name);
    }

    public String doSayHello2(String name) {
        return demoService.sayHello2(name);
    }

    public User getUser(String accessKey){
        User invokeUser = inneruserService.getInvokeUser(accessKey);
        return invokeUser;
    }
}
