package com.example.alarmdrools.feign;

import com.example.alarmdrools.model.dto.CreatWorkOrderResponseDTO;
import com.example.alarmdrools.model.dto.WorkOrderDetailDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(value = "service-manager")
public interface FeignServiceManager {

    @PostMapping(value = "workorders/create-work-order")
     CreatWorkOrderResponseDTO insert(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody WorkOrderDetailDTO workOrderDTO,
            @RequestParam(defaultValue = "false") boolean forceCreate,
            @RequestParam(value = "userId", required = false) String userId);
}
