package com.example.alarmdrools.inteface;

import com.example.alarmdrools.enums.AttributeMethod;
import com.example.alarmdrools.model.dto.Alarm;
import com.example.alarmdrools.model.dto.ProblemAttributeDTO;
import com.example.alarmdrools.model.dto.TicketParams;
import com.example.alarmdrools.model.entity.RuleAttribute;

import java.util.List;

public interface AttributeStrategy {

    List<ProblemAttributeDTO> addTicketAttributes(RuleAttribute ruleAttribute, TicketParams ticketParams, Alarm alarm);

    AttributeMethod getImplementationMethod();
}