package com.example.alarmdrools.model.dto;



import com.example.alarmdrools.enums.Network;
import lombok.Getter;
import lombok.Setter;


import java.util.*;

@Getter
@Setter
public class TicketParams {

    private  EnumSet<Network> networks;
    private  Set <String> technologies;
    private Date eventStartDate;
    private Date eventClearDate;
    private String  eventType;
    private  String siteId;
    private String attributeCategory;

    public TicketParams() {
    }

    public TicketParams(EnumSet<Network> networks,  Set<String> technologies, Date eventStartDate, Date eventClearDate, String eventType, String siteId,
                        String attributeCategory) {
        this.networks = networks;
        this.technologies = technologies;
        this.eventStartDate = eventStartDate;
        this.eventClearDate = eventClearDate;
        this.eventType = eventType;
        this.siteId = siteId;
        this.attributeCategory = attributeCategory;

    }
}
