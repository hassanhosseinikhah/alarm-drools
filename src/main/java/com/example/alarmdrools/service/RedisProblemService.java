package com.example.alarmdrools.service;

import com.example.alarmdrools.model.dto.CreateFaultDTO;
import com.example.alarmdrools.model.dto.ProblemDTO;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class RedisProblemService {

    private HashOperations hashOperations;


    public RedisProblemService(RedisTemplate redisTemplate) {
        this.hashOperations = redisTemplate.opsForHash();
    }


    public void redisdCreateForQuery(List<ProblemDTO> problemDTOS) {

            hashOperations.put("ProblemDTO",1, problemDTOS);
    }

    public List<ProblemDTO> getProblemDTO() {
        return (List<ProblemDTO>) hashOperations.get("ProblemDTO", 1);
    }


    public void redisMyProblemDTO(ProblemDTO problemDTO) {
        hashOperations.put("CreateFaultDTO",problemDTO.getPROM_NUMBER() ,problemDTO);

    }
    public List<ProblemDTO> getMyProblemDTO() {

        Map<Object, ProblemDTO> map =  hashOperations.entries("CreateFaultDTO");
        //        for (ProblemDTO p:problemDTOS) {
//            hashOperations.delete("CreateFaultDTO",p.getPROM_NUMBER());

//        }

        return new ArrayList<>(map.values());

    }
//    public void deleteRedis(ArrayList<ProblemDTO> problemDTOS){
//        for (ProblemDTO p:problemDTOS) {
//            hashOperations.delete("CreateFaultDTO",p.getPROM_NUMBER());
//        }
//    }




}
