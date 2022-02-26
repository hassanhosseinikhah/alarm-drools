package com.example.alarmdrools.model.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Alarm extends AlarmBase{
    private Long alamId;
    private String alamLocnTtname;
    private String alamEqutAbbreviation;
    private String alamEqupIndex;
    private String alamPort;
    private String alamAlmtAbbreviation;
    private String alamMansName;
    private Long alamProcessingType;
    private Date alamDmatimestamp;
    private String alamAlarmType;
    private String alamSubeqp;
    private String alamPortName;
    private String alamPortType;
    private String alamActive;
    private String alamAlasOccurrenceId;
    private String alamProbableCause;//
    private String alamEventType;
    private String alamObjectClass;
    private String alamAreaCode;
    private Date alamReported;
    private String alamSeverity;
    private Date alamAcknowledged;
    private String alamUserAcknowledged;
    private String alamParentNe;

    private String alasInfo;
    private String alasOccurrenceId;
    private Long alasAlamId;
    private Date alasReported;
    private Date alasAcknowledged;
    private Date alasUserClearedTime;
    private Date alasCleared;
    private String alasUserAcknowledged;
    private String alasUserCleared;
    private Long alasPriority;
    private String alasSeverity;
    private String alasMessage;//
    private String alasTriggeringAlarm;
    private String alasProbableRoot;
    private String alasCorrelationId;
    private String alasRootCauseId;
    private String alasObjectId;
    private String alasAlarmId;
    private String alasSertAbbreviation;
    private String alasCirtDisplayName;
    private String alasSuppressedYn;
    private String alasAlthName;
    private Date alasTimestamp;

    /*
    public Alarm(Long alamId, String alamAlarmType, String alamLocnTtname, String alamPort) {
        super();
        this.alamId = alamId;
        this.alamAlarmType = alamAlarmType;
        this.alamLocnTtname = alamLocnTtname;
        this.alamPort = alamPort;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alarm alarm = (Alarm) o;
        return alamId.equals(alarm.alamId) &&
                alasOccurrenceId.equals(alarm.alasOccurrenceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alamId, alasOccurrenceId);
    }
*/
    @Override
    public String toString() {
        return "Alarm{" +
                "alamId=" + alamId +
                ", Locn='" + alamLocnTtname + '\'' +
                ", Equp='" + alamEqupIndex + '\'' +
                ", Type='" + alamAlarmType + '\'' +
                ", Subeqp='" + alamSubeqp + '\'' +
                ", EventType='" + alamEventType + '\'' +
                ", OccurrenceId='" + alasOccurrenceId + '\'' +
                ", repoted='" + alasReported + '\'' +
                ", cleared='" + alasCleared + '\'' +
                '}';
    }

    public long getDuration() {
        long durationValue = 0;
        durationValue = this.alasCleared == null ? (System.currentTimeMillis() - this.alamReported.getTime()) : (this.alasCleared.getTime() - this.alamReported.getTime());
        return durationValue;
    }
/*
    public Interval getInterval() {
        return new Interval(getAlasReported().getTime(), getClearTime());
    }
    private long getClearTime() {
        if (getAlasCleared() == null)
            return Math.max(System.currentTimeMillis(), alasReported.getTime()) + 1;
        else
            return Math.max(alasCleared.getTime(), alasReported.getTime()) + 1;
    }*/


}
