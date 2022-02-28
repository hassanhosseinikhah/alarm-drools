package com.example.alarmdrools.service;

import com.example.alarmdrools.inteface.TokenManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class ScheduleService {


    private final TokenManagerService tokenManagerService;
    private final AlarmService alarmService;


    @Autowired
    public ScheduleService(TokenManagerService tokenManagerService, AlarmService alarmService) {
        this.alarmService = alarmService;
        this.tokenManagerService = tokenManagerService;
    }

    @Scheduled(cron = "* * * * * *")
    public void cronJobSch() throws IOException {

        String token = tokenManagerService.getToken();
        alarmService.getRelatedAlarms(token, null, null, null);
    }


}
