package com.example.alarmdrools.inteface;

import com.example.alarmdrools.model.dto.ProblemAttributeDTO;

import java.util.List;

public interface AttributeUpdate {
//    List<ProblemAttributeDTO> updateTicketAttributes(SiteTicket siteTicket, Event event);

    default int compareTechs(String tech1, String tech2) {
        if (tech1.length() != 2 || tech2.length() != 2)
            return tech1.compareTo(tech2);
        if (tech1.charAt(1) == tech2.charAt(1))
            return tech1.charAt(0) - tech2.charAt(0);
        if (tech1.charAt(1) == 'L' || tech2.charAt(1) == 'G')
            return 1;
        return -1;
    }
}
