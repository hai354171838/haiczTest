package com.tensquare.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import util.IdWorker;

/**
 * @author:Haicz
 * @date:2019/02/01
 */
@SpringBootApplication
@EnableEurekaClient
public class Base01Application {
    public static void main(String[] args) {
        SpringApplication.run(Base01Application.class);
    }
    @Bean
    public IdWorker idWorker () {
        return  new IdWorker(1,1);
    }
}
