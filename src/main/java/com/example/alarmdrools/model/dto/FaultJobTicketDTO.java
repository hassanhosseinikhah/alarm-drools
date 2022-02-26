package com.example.alarmdrools.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author amin
 */
@Data
public class FaultJobTicketDTO {
    private String id;
    @NotNull(message = "JOB Work Group is Required")
    private String fsmWorkGroup;
    @NotNull(message = "FSM Priority is Required")
    private String fsmPriority;
    @NotNull(message = "FSM Customer Appointment Date is Required")
    private Date fsmCustomerAppointmentDate;
    @NotNull(message = "Region is Mandatory to create a Job Ticket")
    private String areaCode;
    @NotNull(message = "Customer is Required")
    private String customer;
    @NotNull(message = "Description is Required")
    private String description;
    private Date proposedStartDate;
    private Date proposedFinishDate;

    public FaultJobTicketDTO(){
    }

    public FaultJobTicketDTO(@NotNull(message = "JOB Work Group is Required") String fsmWorkGroup, @NotNull(message = "FSM Priority is Required") String fsmPriority, @NotNull(message = "FSM Customer Appointment Date is Required") Date fsmCustomerAppointmentDate, @NotNull(message = "Region is Mandatory to create a Job Ticket") String areaCode, @NotNull(message = "Customer is Required") String customer, @NotNull(message = "Description is Required") String description) {
        this.fsmWorkGroup = fsmWorkGroup;
        this.fsmPriority = fsmPriority;
        this.fsmCustomerAppointmentDate = fsmCustomerAppointmentDate;
        this.areaCode = areaCode;
        this.customer = customer;
        this.description = description;
    }
}

