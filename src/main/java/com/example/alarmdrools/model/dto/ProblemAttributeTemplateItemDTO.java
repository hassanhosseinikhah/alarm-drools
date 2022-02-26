package com.example.alarmdrools.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Azar Amiri
 * 15 April 2019 - 12:09
 */
@Getter
@Setter
@NoArgsConstructor
public class ProblemAttributeTemplateItemDTO {
    private String template;
    private String attribute;
    private Long displayOrder;
    private String optionality;
    private String dataType;
    private boolean hasLov;
}
