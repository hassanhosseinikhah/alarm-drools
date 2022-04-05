package com.example.alarmdrools.service;

import com.example.alarmdrools.inteface.TokenManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;


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


    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void cronJobSch() throws IOException {

        log.info("****************************************************");
        log.info("****START cronJobSch");
        String token = tokenManagerService.getToken();
        alarmService.createFJW(token, null, null, null);
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
