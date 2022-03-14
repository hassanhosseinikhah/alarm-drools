package com.example.alarmdrools.service;

import com.example.alarmdrools.model.dto.ProblemDTO;
import com.example.alarmdrools.model.dto.ProblemStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ProblemService {

    private final EntityManager entityManager;

    public ProblemService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    public List<ProblemDTO> createProblem() {
        log.info("********Start CreateProblem By Query*************");

        String query = "select * from CLARITY.MVW_OPEN_TICKET_HISTORY";


        List<ProblemDTO> problemDTOS = entityManager.createNativeQuery(query, "ProblemMapping").getResultList();
        log.info("********End CreateProblem By Query*************  " + problemDTOS.size());
        return problemDTOS;


    }

    public List<ProblemStatus> checkStatus(Long promNumber) {
        log.info("********Start CheckStatus By Query*************");
        List<ProblemStatus> problemStatuses = new ArrayList<>();


        String query = String.format("select PROM_NUMBER,PROM_PROS_CODE from CLARITY.PROBLEMS where PROM_PROS_CODE not in(2025,1004) and PROM_NUMBER=%d", promNumber);
        problemStatuses = entityManager.createNativeQuery(query, "ProblemStatusMapping").getResultList();
        log.info("********End CheckStatus By Query*************");
        return problemStatuses;
    }

}
