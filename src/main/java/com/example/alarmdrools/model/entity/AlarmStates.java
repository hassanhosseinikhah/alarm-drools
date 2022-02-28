package com.example.alarmdrools.model.entity;

import com.example.alarmdrools.model.dto.AlarmStatesID;
import com.google.gson.Gson;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Alarm_States")
@IdClass(AlarmStatesID.class)
@Data
public class AlarmStates {
    public static final String FAKE_STATUS = "F";

    @Id
    @Column(name = "ALAS_OCCURRENCEID", length = 40)
    private String alasOccurrenceId;
    @Id
    @Column(name = "ALAS_ALAM_ID")
    private Long alasAlamId;
    @Column(name = "ALAS_REPORTED")
    private Date alasReported;
    @Column(name = "ALAS_ACKNOWLEDGED")
    private Date alasAcknowledged;
    @Column(name = "ALAS_USERCLEAREDTIME")
    private Date alasUserClearedTime;
    @Column(name = "ALAS_CLEARED")
    private Date alasCleared;
    @Column(name = "ALAS_USERACKNOWLEDGED", length = 20)
    private String alasUserAcknowledged;
    @Column(name = "ALAS_USERCLEARED", length = 20)
    private String alasUserCleared;
    @Column(name = "ALAS_PRIORITY")
    private String alasPriority;
    @Column(name = "ALAS_SEVERITY", length = 15)
    private String alasSeverity;

    //we retrieve it from MDD under name ADHOC_DESCRIPTION
    @Column(name = "ALAS_MESSAGE", length = 1500)
    private String alasMessage;
    @Column(name = "ALAS_TRIGGERINGALARM", length = 40)
    private String alasTriggeringAlarm;
    @Column(name = "ALAS_PROBABLEROOT", length = 40)
    private String alasProbableRoot;
    @Column(name = "ALAS_CORRELATIONID", length = 40)
    private String alasCorrelationId;
    @Column(name = "ALAS_ROOTCAUSEID", length = 40)
    private String alasRootCauseId;
    @Column(name = "ALAS_OBJECTID", length = 40)
    private String alasObjectId;
    @Column(name = "ALAS_ALARMID", length = 40)
    private String alasAlarmId;
    @Column(name = "ALAS_SERT_ABBREVIATION", length = 30)
    private String alasSertAbbreviation;
    @Column(name = "ALAS_CIRT_DISPLAYNAME", length = 55)
    private String alasCirtDisplayName;
    @Column(name = "ALAS_SUPPRESSED_YN", length = 1)
    private String alasSuppressedYn;
    @Column(name = "ALAS_ALTH_NAME", length = 30)
    private String alasAlthName;
    @Column(name = "ALAS_TIMESTAMP")
    private Date alasTimestamp;
    @Column(name = "ALAS_INFO", length = 1)
    private String alasInfo;

    public static AlarmStates deepCopyAlarmStates(AlarmStates alarmStatesToCopy) {
        Gson gson = new Gson();
        return gson.fromJson(gson.toJson(alarmStatesToCopy), AlarmStates.class);
    }

    public boolean isFakeOffTime() {
        return alasInfo != null && alasInfo.equals("F");
    }

    public boolean isActive() {
        return this.alasCleared == null;
    }
}
