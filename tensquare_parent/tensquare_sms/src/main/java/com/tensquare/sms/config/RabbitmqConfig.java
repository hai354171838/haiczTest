package com.tensquare.sms.config;


import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author:Haicz
 * @date:2019/03/03
 */
@Configuration
public class RabbitmqConfig {
    //消费者并并发数
    public static final int DEFAULT_CONCURRENT=10;
    @Bean(value = "customContainerFactory")
    public SimpleRabbitListenerContainerFactory  containerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory){

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConcurrentConsumers(DEFAULT_CONCURRENT);
        factory.setMaxConcurrentConsumers(DEFAULT_CONCURRENT);
        configurer.configure(factory,connectionFactory);
        return  factory;

    }

}
