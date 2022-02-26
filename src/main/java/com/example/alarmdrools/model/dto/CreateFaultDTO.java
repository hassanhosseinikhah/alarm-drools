package com.example.alarmdrools.model.dto;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author barzideh
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class CreateFaultDTO {
    private Long number;
    @NotNull(message = "The Fault Reported Date parameter is mandatory.")
    private Date reportedTime;
    @NotNull(message = "The Fault Reported By parameter is mandatory.")
    private String reportedby;
    @NotNull(message = "The Priority parameter is mandatory.")
    private Long priority;
    @Length(max = 250, message = "Max length for 'Description' is 250 char.")
    @NotNull(message = "The Fault Description parameter is mandatory.")
    private String description;
    private String worgName;
    @NotNull(message = "Fault Cause parameter is mandatory.")
    private String cause;
    private String entity;
    private String impact;
    private Date acknowledged;
    private String acknowledgedBy;
    private String regnCode;
    private String mansName;
    private Date created;
    private String creatingWorgName;
    private String nccLocation;
    private String nccNetwork1;
    private String nccNetwork2;
    private String nccAction;
    private String platformProblem;
    private String nccCause;
    @NotNull(message = "Fault Type parameter is mandatory.")
    private String type;
    private String assignedTo;
    private String category;
    private String subCategory;
    @Length(max = 20, message = "Max length for 'Contact' is 20 char.")
    private String reportedContact;
    private List<LinksDTO> links;
    private List<ProblemAttributeDTO> attributes;
    private List<AttachmentDTO> attachments;

    @AssertTrue(message = "Reported time should be before current time")
    public boolean isValidReportedTime() {
        if (reportedTime != null)
            return reportedTime.before(new Date(System.currentTimeMillis() + 300000));
        else
            return false;
    }
}
