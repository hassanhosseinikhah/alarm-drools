package com.example.alarmdrools.service;

import com.example.alarmdrools.inteface.TokenManagerService;
import com.example.alarmdrools.model.dto.CreateFaultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class ScheduleService {


    private final TokenManagerService tokenManagerService;

    private final AlarmService alarmService;


    @Autowired
    public ScheduleService(TokenManagerService tokenManagerService, AlarmService alarmService) {
        this.alarmService = alarmService;
        this.tokenManagerService = tokenManagerService;
    }

    @Scheduled(cron = "*/10 * * * * *")
    public void cronJobSch() throws IOException {

        String token = tokenManagerService.getToken();
        alarmService.getRelatedAlarms(token, null, null, null);
    }


}
