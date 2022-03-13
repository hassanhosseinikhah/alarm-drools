package com.example.alarmdrools.map;

import com.example.alarmdrools.model.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.persistence.EntityManager;
import java.util.List;

@Service
@Slf4j
public class ConvertToDTO {

    private EntityManager entityManager;

    @Autowired
    public ConvertToDTO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public ProblemDTO MapToProlemDto(CreateFaultDTO createFaultDTO, Alarm alarm){
        ProblemDTO problemDTO= new ProblemDTO();
        problemDTO.setPROM_NUMBER(createFaultDTO.getNumber());
        problemDTO.setALAM_ALARMTYPE(alarm.getAlamAlarmType());
        problemDTO.setPROM_CAUSE(alarm.getCause());
        problemDTO.setPROM_CREATED(createFaultDTO.getCreated());
        problemDTO.setPROM_REPORTED(createFaultDTO.getReportedTime());
        problemDTO.setPRAT_VALUE(getValue(createFaultDTO));

        return  problemDTO;

    }
    private String getValue(CreateFaultDTO createFaultDTO){
     String query=String.format("select PRAT_VALUE from CLARITY.PROBLEM_ATTRIBUTES  where  PRAT_PRAL_NAME='HUB / SITE / NODE ID' and PRAT_PROM_NUMBER=%d",createFaultDTO.getNumber());
     List<Attributes> value=entityManager.createNativeQuery(query,"AttributesValue").getResultList();
     return value.get(0).getPRAT_VALUE();

    }
}
