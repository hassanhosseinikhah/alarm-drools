package com.example.alarmdrools.map;

import com.example.alarmdrools.model.dto.*;
import com.example.alarmdrools.service.AlarmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.persistence.EntityManager;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        problemDTO.setPRAT_VALUE(getLocation(alarm));

        return  problemDTO;

    }


    public String getLocation(Alarm alarm) {

        Pattern LocnPattern = Pattern.compile(".*([A-Z]{2})(\\d{4}).*");
        if (alarm.getAlamLocnTtname() != null) {
            Matcher matcherLocnTtname = LocnPattern.matcher(alarm.getAlamLocnTtname());
            if (matcherLocnTtname.matches()) {
                return matcherLocnTtname.group(1) + matcherLocnTtname.group(2);
//
            }
        }
        if (alarm.getAlamEqupIndex() != null) {
            Matcher matcherEqupIndex = LocnPattern.matcher(alarm.getAlamEqupIndex());
            if (matcherEqupIndex.matches()) {
                return matcherEqupIndex.group(1) + matcherEqupIndex.group(2);
            }
        }
        if (alarm.getAlamEventType() != null) {
            Matcher matcherEventType = LocnPattern.matcher(alarm.getAlamEventType());
            if (matcherEventType.matches()) {
                return matcherEventType.group(1) + matcherEventType.group(2);
            }

        }
        if (alarm.getAlasMessage() != null) {
            Matcher matcherAlasMessage = LocnPattern.matcher(alarm.getAlasMessage());
            if (matcherAlasMessage.matches()) {
                return matcherAlasMessage.group(1) + matcherAlasMessage.group(2);
            }
        }
        if (alarm.getAlamPort() != null) {
            Matcher matcherAlamPost = LocnPattern.matcher(alarm.getAlasMessage());
            if (matcherAlamPost.matches()) {
                return matcherAlamPost.group(1) + matcherAlamPost.group(2);
            }

        }

        return null;

    }
}
