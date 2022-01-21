package com.wenwo.evaluate;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@EnableApolloConfig
@EnableFeignClients(basePackages = "com.wenwo")
@MapperScan("com.wenwo.evaluate.dao")
@SpringBootApplication
public class EvaluateApplication {

    public static void main(String[] args) {
        SpringApplication.run(EvaluateApplication.class, args);
    }

}
