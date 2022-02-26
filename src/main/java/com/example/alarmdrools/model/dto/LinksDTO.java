package com.example.alarmdrools.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author m.barzideh
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinksDTO {
    @NotNull
    private String foreignId;
    @NotNull
    private String foreignType;
    private String foreignParent;
}