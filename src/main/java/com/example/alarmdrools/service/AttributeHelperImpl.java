package com.example.alarmdrools.service;


import com.example.alarmdrools.exception.RuleAttributeException;
import com.example.alarmdrools.inteface.AttributeHelper;
import com.example.alarmdrools.inteface.AttributeStrategy;
import com.example.alarmdrools.inteface.MessageByLocaleService;
import com.example.alarmdrools.model.dto.Alarm;
import com.example.alarmdrools.model.dto.ProblemAttributeDTO;
import com.example.alarmdrools.model.dto.TicketParams;
import com.example.alarmdrools.model.entity.RuleAttribute;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttributeHelperImpl implements AttributeHelper {
    private  List<AttributeStrategy> attributeStrategies;
    private final MessageByLocaleService messages;

    public AttributeHelperImpl(List<AttributeStrategy> attributeStrategies,MessageByLocaleService messages ) {
        this.attributeStrategies = attributeStrategies;
        this.messages = messages;
    }

    @Override
    public List<ProblemAttributeDTO> getTicketAttributes(RuleAttribute ruleAttribute, TicketParams ticketParams, Alarm alarm) {
        AttributeStrategy implementingMethod = attributeStrategies.stream()
                .filter(ab -> ab.getImplementationMethod().equals(ruleAttribute.getMethod()))
                .findFirst().orElseThrow(() -> new RuleAttributeException.NotFound(
                        messages.getMessage("processor.attribute.not.found"), ruleAttribute.getMethod()));
        return implementingMethod.addTicketAttributes(ruleAttribute, ticketParams ,alarm);
    }
}
