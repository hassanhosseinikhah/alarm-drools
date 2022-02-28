package com.example.alarmdrools.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Azar Amiri
 * 08 April 2019 - 14:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmStatesID implements Serializable {
    private String alasOccurrenceId;
    private long alasAlamId;
}
