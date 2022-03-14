package com.example.alarmdrools.service;

import com.example.alarmdrools.inteface.TokenManagerService;
//import com.example.alarmdrools.repository.ProblemRepsitory;
import com.example.alarmdrools.model.dto.ProblemDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;


@Component
@Slf4j
public class ScheduleService {


    private final TokenManagerService tokenManagerService;
    private final AlarmService alarmService;
    private final ProblemService problemService;
    private final RedisProblemService redisProblemService;


    public ScheduleService(TokenManagerService tokenManagerService, AlarmService alarmService, ProblemService problemService, RedisProblemService redisProblemService) {
        this.tokenManagerService = tokenManagerService;
        this.alarmService = alarmService;
        this.problemService = problemService;
        this.redisProblemService = redisProblemService;
    }


    @Scheduled(cron = "* */2 * * * *")
    public void cronJobSch() throws IOException {

        log.info("****************************************************");
        log.info("****START cronJobSch");
        String token = tokenManagerService.getToken();
        alarmService.getRelatedAlarms(token, null, null, null);
        log.info("****END cronJobSch");
        log.info("**********************************************************");

    }

    //    @Scheduled(cron = "* */2 * * * *")
//    public void schedulerForRedisInsert(){
//        List<ProblemDTO> problemDTOS=problemService.createProblem();
//        redisProblemService.redidCreate(problemDTOS);
//        alarmService.getProblem();
//    }
//    @Scheduled(cron = "0 0 1,13 * * *")
//    public void schedulerForRedisInsert() {
//        List<ProblemDTO> problemDTOS = problemService.createProblem();
//        redisProblemService.redisdCreateForQuery(problemDTOS);
//
//    }
//    @Scheduled(cron = "* */2 * * * *")
//    public void schedulerForCrateLink() {
//     problemService.checkStatus( 10665517L);
//    }


}
