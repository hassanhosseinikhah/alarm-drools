package com.example.alarmdrools.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class RuleAttributeId implements Serializable {
    private String category;
    private String name;
}
