package com.example.alarmdrools.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class JobTicketDetailDTO {

    private Long id;

    private String workGroup;
    private String description;
    private String status;
    private Character executionStatus;
    private String customerId;
    private String customerName;
    private String customerType;
    private String customerContactNumber;
    private String preferredInternalAgentId;
    private Date customerAppointment;
    private Date receivedDate;
    private Date proposedStartDate;
    private Date proposedFinishDate;
    private Date expectedStartDate;
    private Date expectedEndDate;
    private Date actualStartDate;
    private Date actualCompleteDate;
    private String actualDuration;
    private Date lastUpdatedOn;
    private String lastUpdatedSystem;
    private String componentCategory;


    private Long addressId;
    private String areaCode;
    private String locationCode;
    private String circuitName;
    private String componentType;
    private String componentIndex;
    private String frameUnitName;
    private String cableCore;


    private String foreignType;
    private String parentKey;
    private String childKey;
    private String serviceType;
    private String jobTicketType;
    private String TicketOldPriority;
    private String TicketNewPriority;
    private String wfmPriority;
    private String slaCode;


}

