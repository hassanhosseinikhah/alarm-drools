package com.example.alarmdrools.model.dto;

import com.example.alarmdrools.enums.DataType;
import com.example.alarmdrools.enums.Optionality;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import org.hibernate.validator.constraints.Length;





@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProblemAttributeDTO {

    private String pralName;
    @Length(max = 1000, message = "Max length for attribute value is 1000.")
    private String value;
    private Optionality optionality;
    private Long displayOrder;
    private DataType dataType;
    private boolean hasLov = false;


    public ProblemAttributeDTO() {
    }

    public ProblemAttributeDTO(String pralName, String value, Optionality optionality, Long displayOrder, DataType dataType) {
        this.pralName = pralName;
        this.value = value;
        this.optionality = optionality;
        this.displayOrder = displayOrder;
        this.dataType = dataType;
    }

    public ProblemAttributeDTO(String pralName, String value) {
        this.pralName = pralName;
        this.value = value;
    }
}
