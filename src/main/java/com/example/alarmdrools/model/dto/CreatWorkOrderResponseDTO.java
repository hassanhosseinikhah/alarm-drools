package com.example.alarmdrools.model.dto;

import lombok.Data;

@Data
public class CreatWorkOrderResponseDTO {
    private WorkOrderDetailDTO workOrder;
    private String message;
    private boolean status;
}
