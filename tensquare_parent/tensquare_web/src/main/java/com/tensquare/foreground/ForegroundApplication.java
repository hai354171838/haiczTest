package com.tensquare.foreground;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @author:Haicz
 * @date:2019/03/08
 */
@EnableZuulProxy//开启网关的主键
@EnableEurekaClient
@SpringBootApplication
public class ForegroundApplication {
    public static void main(String[] args) {
        SpringApplication.run(ForegroundApplication.class,args);
    }

}
