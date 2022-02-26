package com.example.alarmdrools.model.dto;


import com.example.alarmdrools.enums.JobTicketForeignType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author barzideh
 */
@Data
@NoArgsConstructor
public class ActivityJobTicketDTO {
    private String id;
    @NotNull(message = "JOB Work Group is Required")
    private String workGroup;
    @NotNull(message = "JOB Description is Required")
    private String description;
    @NotNull(message = "JOB Address ID is NULL")
    private Long addressId;
    @NotNull(message = "Area Code is Required")
    private String areaCode;
    private String status;
    private String locationCode;
    private String circuitName;
    private String componentType;
    private String componentIndex;
    private String frameUnitName;
    private String cableCore;
    @NotNull(message = "JOB Ticket Type is Required")
    private String jobTicketType;
    private String wfmPriority;
    private String slaCode;
    private String customerId;
    private String customerContactNumber;
    private Date customerAppointmentDate;
    private Date proposedStartDate;
    private Date proposedFinishDate;
    private String preferredInternalAgentId;
    private String comments;
    @NotNull(message = "JOB FOREIGN_TYPE is Required")
    private JobTicketForeignType foreignType;
}
