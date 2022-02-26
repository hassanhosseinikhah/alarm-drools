package com.example.alarmdrools.feign;

import com.clarity.alarmaction.action.ActionResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Component
@FeignClient(value = "alarm-action-service")
public interface FeignAlarmAction {

    @PostMapping("action/{type}")
    ActionResult doAlarmAction(@RequestHeader(value = "Authorization") String token,
                               @PathVariable("type") String type,
                               @RequestBody List<String> occurrenceIds,
                               @RequestParam(value = "severity", required = false) String severity,
                               @RequestParam(value = "comment", required = false) String comment);

    @PostMapping("action/ack/{type}")
     ActionResult doAlarmAck(@RequestHeader(value = "Authorization") String token,
                                   @PathVariable("type") String type,
                                   @RequestBody List<String> occurrenceIds,
                                   @RequestParam(value = "severity", required = false) String severity,
                                   @RequestParam(value = "comment", required = false) String comment);
}