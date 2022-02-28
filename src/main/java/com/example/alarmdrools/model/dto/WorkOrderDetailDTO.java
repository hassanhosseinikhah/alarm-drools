package com.example.alarmdrools.model.dto;


import com.example.alarmdrools.enums.ParkColor;
import com.example.alarmdrools.enums.WorkOrderType;
import lombok.Data;

import java.util.Date;


/**
 * @author Akhtar Sabzi
 * 11 May 2019 - 11:30
 */

@Data
public class WorkOrderDetailDTO extends WorkOrderDTO {

    private Long trunkGroupId;
    private String troubleTicketNumber;
    private Long peTaskId;
    private Long plannedEventTaskWorkId;
    private Long plannedEventFallbackId;
    private String empeId;
    private String servId;
    private Long promNumber;
    private String statusAbbreviation;
    private Long taskId;
    private WorkOrderType type;
    private Long stepNumber;

    private ParkColor parkColor;
    private Date parkDueDate;

}
