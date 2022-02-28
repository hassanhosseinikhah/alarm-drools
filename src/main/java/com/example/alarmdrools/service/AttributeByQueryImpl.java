package com.example.alarmdrools.service;


import com.example.alarmdrools.enums.AttributeMethod;
import com.example.alarmdrools.inteface.AttributeStrategy;
import com.example.alarmdrools.model.dto.Alarm;
import com.example.alarmdrools.model.dto.ProblemAttributeDTO;
import com.example.alarmdrools.model.dto.TicketParams;
import com.example.alarmdrools.model.entity.RuleAttribute;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;

@Service

public class AttributeByQueryImpl implements AttributeStrategy {


    private EntityManager entityManager;
    @Value("${query.queryParam1}")
    private String queryParam1;

    @Value("${query.queryParam2}")
    private String queryParam2;

    @Value("${query.queryParam3}")
    private String queryParam3;

    @Autowired
    public  AttributeByQueryImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public List<ProblemAttributeDTO> addTicketAttributes(RuleAttribute ruleAttribute, TicketParams ticketParams, Alarm alarm) {
//        Alarm mainAlarm = ticketParams.getMainAlarm();
        if (StringUtils.isEmpty(ruleAttribute.getValue()))
            return createDTO(ruleAttribute, "");
        else if (!ruleAttribute.getValue().trim().toUpperCase().startsWith("SELECT "))
            return createDTO(ruleAttribute, ruleAttribute.getValue().replace(queryParam3, ticketParams.getSiteId()));
        String query = ruleAttribute.getValue().replace(queryParam1, alarm.getAlasOccurrenceId())
                .replace(queryParam2, String.valueOf(alarm.getAlamId()))
                .replace(queryParam3, ticketParams.getSiteId());
        String result = null;
        try {
//            log.info("Attribute {} query: {}", ruleAttribute.getName(), query);
            result = String.valueOf(entityManager.createNativeQuery(query).getSingleResult());
        } catch (NoResultException ex) {
//            log.error("Error while getting attribute value, Category: '{}' name: '{}'; message: {}",
//                    ruleAttribute.getCategory(), ruleAttribute.getName(), ex.getMessage());
        }
        return createDTO(ruleAttribute, result);
    }

    private List<ProblemAttributeDTO> createDTO(RuleAttribute ruleAttribute, String value) {
        return Collections.singletonList(new ProblemAttributeDTO(ruleAttribute.getName(), value));
    }

    @Override
    public AttributeMethod getImplementationMethod() {
        return AttributeMethod.QUERY;
    }
}
