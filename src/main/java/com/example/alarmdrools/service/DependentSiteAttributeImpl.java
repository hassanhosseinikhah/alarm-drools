package com.example.alarmdrools.service;
import com.example.alarmdrools.enums.AttributeMethod;
import com.example.alarmdrools.inteface.AttributeStrategy;
import com.example.alarmdrools.inteface.AttributeUpdate;
import com.example.alarmdrools.model.dto.Alarm;
import com.example.alarmdrools.model.dto.ProblemAttributeDTO;
import com.example.alarmdrools.model.dto.TicketParams;
import com.example.alarmdrools.model.entity.RuleAttribute;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DependentSiteAttributeImpl implements AttributeStrategy, AttributeUpdate {

    private static final String ATTRIBUTE_NAME = "DEPENDENT SITE LIST";
    private static final String EXT_ATTRIBUTE_NAME = "DEPENDENT SITE ";
    private static final char SEP_CHAR = ' ';


    @Override
    public List<ProblemAttributeDTO> addTicketAttributes(RuleAttribute ruleAttribute, TicketParams ticketParams, Alarm alarm) {
        return getDependentAttributes(getDependents(ticketParams.getSiteId(), ticketParams.getTechnologies()));
    }

    private String getDependents(String siteId, Set<String> techs) {
        String sitePrefix = siteId.substring(0, 2);
        String sitePostfix = siteId.substring(2, 6);
        return techs.stream().sorted(this::compareTechs).map(tech -> sitePrefix + (tech.equals("UG") ? "G" : tech) + sitePostfix)
                .collect(Collectors.joining(String.valueOf(SEP_CHAR)));
    }
//
//    @Override
//    public List<ProblemAttributeDTO> updateTicketAttributes(SiteTicket siteTicket, Event event) {
//        Set<String> siteTechs = siteTicket.getSiteToTechs().get(event.getSiteId());
//        if (!CollectionUtils.isEmpty(siteTechs) && !siteTechs.containsAll(event.getTechnologies())) {
//            StringBuilder depBuilder = new StringBuilder();
//            for (Map.Entry<String, Set<String>> siteToTechs : siteTicket.getSiteToTechs().entrySet()) {
//                if (siteToTechs.getKey().equals(event.getSiteId())) {
//                    Set<String> techs = new HashSet<>(siteTechs);
//                    techs.addAll(event.getTechnologies());
//                    depBuilder.append(getDependents(event.getSiteId(), techs)).append(SEP_CHAR);
//                } else depBuilder.append(getDependents(siteToTechs.getKey(), siteToTechs.getValue())).append(SEP_CHAR);
//            }
//            if (depBuilder.length() > 0) depBuilder.deleteCharAt(depBuilder.length() - 1);
//            return getDependentAttributes(depBuilder.toString());
//        }
//        return new ArrayList<>();
//    }

    private List<ProblemAttributeDTO> getDependentAttributes(String dependents) {
        List<ProblemAttributeDTO> results = new ArrayList<>();
        int partIndex = 0;
        int startIndex = 0;
        int endIndex = Math.min(999, dependents.length() - 1);
        while (endIndex > startIndex && partIndex < 10) {
            endIndex = moveIndexToLastSeparator(dependents, startIndex, endIndex);
            if (endIndex > startIndex) {
                String dependentPart = dependents.substring(startIndex, endIndex + 1);
                if (partIndex == 0)
                    results.add(new ProblemAttributeDTO(ATTRIBUTE_NAME, dependentPart));
                else
                    results.add(new ProblemAttributeDTO(EXT_ATTRIBUTE_NAME + partIndex, dependentPart));
                startIndex = endIndex + 1;
                endIndex = Math.min(endIndex + 1000, dependents.length() - 1);
                partIndex++;
            }
        }
        return results;
    }

    private int moveIndexToLastSeparator(String dependents, int startIndex, int endIndex) {
        if (endIndex != dependents.length() - 1)
            while (dependents.charAt(endIndex) != SEP_CHAR && endIndex > startIndex) endIndex--;
        return endIndex;
    }

    @Override
    public AttributeMethod getImplementationMethod() {
        return AttributeMethod.DEPENDENT_SITE;
    }
}
