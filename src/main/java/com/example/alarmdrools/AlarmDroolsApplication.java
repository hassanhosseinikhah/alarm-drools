package com.example.alarmdrools;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;



@SpringBootApplication(scanBasePackages = {"com.example.alarmdrools","com.clarity.cloud.common.feign.service"})
@EnableEurekaClient
@EnableCircuitBreaker
@EnableFeignClients(basePackages ={"com.example.alarmdrools.feign","com.clarity.cloud.common.feign.service"})
@EnableScheduling
public class AlarmDroolsApplication {


    public static void main(String[] args) {

        SpringApplication.run(AlarmDroolsApplication.class, args);
    }

}
