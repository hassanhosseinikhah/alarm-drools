package com.example.alarmdrools;

import com.clarity.cloud.common.config.AsyncConfig;
import com.clarity.cloud.common.config.MessageConfig;
import com.clarity.cloud.common.config.WebMvcConfiguration;
import com.clarity.cloud.common.config.WebSecurityConfig;
import com.example.alarmdrools.model.ApplicationVariables;
import com.example.alarmdrools.service.AlarmService;
import com.example.alarmdrools.service.TokenManagerServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages ={"com.example.alarmdrools.feign","com.clarity.cloud.common.feign.service"})
@Slf4j
@Import({WebSecurityConfig.class, AsyncConfig.class, WebMvcConfiguration.class, MessageConfig.class})
public class AlarmDroolsApplication implements ApplicationRunner {


    private final ApplicationVariables appVars;
    private  final AlarmService alarmService;
    private final TokenManagerServiceImpl tokenManagerService;


    public AlarmDroolsApplication(ApplicationVariables appVars, AlarmService alarmService, TokenManagerServiceImpl tokenManagerService) {
        this.appVars = appVars;
        this.alarmService = alarmService;
        this.tokenManagerService = tokenManagerService;
    }
    public static void setIsFirstIteration(boolean isFirstIteration) {
        AlarmDroolsApplication.isFirstIteration = isFirstIteration;
    }

    private static boolean isFirstIteration;

    public static boolean isIsFirstIteration() {
        return isFirstIteration;
    }

    public static void main(String[] args) {
        SpringApplication.run(AlarmDroolsApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        setIsFirstIteration(true);
        while (true) {
            try {
                long expectedEndTime = System.currentTimeMillis() + appVars.getEventRefreshInterval();
                log.warn("\n*********************************  Iteration Started  *********************************");

                alarmService.getRelatedAlarms(tokenManagerService.getToken(),null,null,null);

                log.warn("\n*********************************  Iteration Finished *********************************");
                sleepUntilExpectedTime(expectedEndTime);
                setIsFirstIteration(false);
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        }
    }

    private void sleepUntilExpectedTime(long expectedEndTime) {
        long remainingTime = expectedEndTime - System.currentTimeMillis();
        if (remainingTime > 100) {
            try {
                Thread.sleep(remainingTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
