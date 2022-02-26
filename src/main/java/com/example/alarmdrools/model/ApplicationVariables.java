package com.example.alarmdrools.model;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "variables")
@Getter
@Setter
public class ApplicationVariables {
    //unused
    private List<Long> inactiveTicketStates;
    private List<String> fluctuationCauses;
    private Map<String, List<String>> relatedCauseMap;
    private Long fluctuatingTicketDuration;
    //used
    private Long fluUpdateAllowed;
    private Long fluLinkAllowed;
    private Long downUpdateAllowed;
    private Long downLinkAllowed;
    private Long siteDownAlarmDuration;
    private Long cellDownAlarmDuration;
    private Long cellToSiteAllowed;
    private Long ticketReportedOffset;
    private Integer alarmActionBatchSize;
    private Long downTicketCreateJobTicketAllowed;
    private Long fluCreateJobTicketAllowed;

    private String ticketWorkGroup;
    private String ticketType;
    private String ticketEntity;
    private String ticketImpact;

    private Set<String> band900;
    private Long relatedAlarmDuration;
    private Long linkAlarmDuration;
    private Long fluMinDuration;
    private Long fluMinDownTime;
    private Long fluCreateAllowed;

    private String userName;
    private String userId;
    private String creatorWorkGroup;
    private String jobCreator;
    private long inventoryRefreshInterval;
    private long fluToDownCheckInterval;
    private long eventRefreshInterval;
    private int eventProcessorThreads;
    private long jobTicketDelay;
    private Map<String, String> contractorToFSMGroup;
    private Set<String> additionalDownCauses;
    private Set<String> NICause;
    private String impactsIgnoringAttributeUpdate;
    private String NIComment;
    private String jobTicketComment;
    private String indicator2G;

    private String filterSeparator;

    private Long headRangeOfLevel4CacheQuery;
    private Long endRangeOfLevel4CacheQuery;
    private Long headRangeOfOldLevel4CacheQuery;
    private Set<String> provinceInCacheQuery;
}