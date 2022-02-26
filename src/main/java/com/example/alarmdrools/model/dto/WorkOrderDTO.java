package com.example.alarmdrools.model.dto;


import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Akhtar Sabzi
 * 02 March 2019 - 14:04
 */
@Data
public class WorkOrderDTO {
//    private WorkOrderIdDTO id;
    private String serviceOrderNumber;
    private Long seroRevision;
    private String soExternalId;
    private String workGroup;
    private String orderType;
    private String createdBy;
    private Date createDate;
    private String reviewedBy;
    private Date reviewDate;
    private String approvedBy;
    private Date approvedDate;
    private Date statusDate;
    private String userUpdated;
    private String taskName;
    private Date assignedDate;
    private Date proposedStartDate;
    private Date proposedEndDate;
    private Date actualStartDate;
    private Date actualEndDate;
    private String areaCode;
    private String nodeCategory;
    private String description;
    private String employeeAssignedTo;
    private boolean editable;
//    private ProblemDTO problems;
//    private PlannedEventDTO plannedEvents;
//    private List<CommentDTO> comments;
//    private List<ServiceOrderAttributeDTO> attributes;
    private List<AttachmentDTO> attachments;
//    private List<WorkOrderActivityDTO> activities;

    public void setStatusDateIfNull(Date date) {
        if (this.statusDate == null)
            this.statusDate = date;
    }
}
