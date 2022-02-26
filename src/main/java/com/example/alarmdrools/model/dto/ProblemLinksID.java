package com.example.alarmdrools.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
public class ProblemLinksID implements Serializable {
    private Long id;
    private String foreignId;
    private String foreignType;
}
