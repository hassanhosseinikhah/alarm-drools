package com.example.alarmdrools.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


@Getter
@Setter
@Entity
@Table(name = "Alarms")
public class Alarms implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ALAM_ID", nullable = false)
    private long alamId = -1;

    @Column(name = "ALAM_LOCN_TTNAME", nullable = false)
    private String alamLocnTtname;

    //It is sent by this name from MDD : //EQP_TYPE
    @Column(name = "ALAM_EQUT_ABBREVIATION", nullable = false)
    private String alamEqutAbbreviation;

    @Column(name = "ALAM_EQUP_INDEX", nullable = false)
    private String alamEqupIndex;

    @Column(name = "ALAM_PORT")
    private String alamPort;

    //It is sent by this name from MDD : STD_PORTALARM_TYPE
    @Column(name = "ALAM_ALMT_ABBREVIATION", nullable = false)
    private String alamAlmtAbbreviation;

    @Column(name = "ALAM_MANS_NAME", nullable = false)
    private String alamMansName;

    @Column(name = "ALAM_PROCESSINGTYPE")
    private String alamProcessingType;

    @Column(name = "ALAM_DMATIMESTAMP")
    private Date alamDmatimestamp;

    @Column(name = "ALAM_ALARMTYPE")
    private String alamAlarmType;

    @Column(name = "ALAM_SUBEQP")
    private String alamSubeqp;

    @Column(name = "ALAM_PORT_NAME")
    private String alamPortName;

    @Column(name = "ALAM_PORTTYPE")
    private String alamPortType;

    @Column(name = "ALAM_ACTIVE")
    private String alamActive;

    @Column(name = "ALAM_ALAS_OCCURRENCEID")
    private String alamAlasOccurrenceId;

    @Column(name = "ALAM_PROBABLECAUSE")
    private String alamProbableCause;

    @Column(name = "ALAM_EVENTTYPE")
    private String alamEventType;

    @Column(name = "ALAM_OBJECTCLASS")
    private String alamObjectClass;

    @Column(name = "ALAM_AREA_CODE")
    private String alamAreaCode;

    @Column(name = "ALAM_REPORTED")
    private Date alamReported;

    @Column(name = "ALAM_SEVERITY")
    private String alamSeverity;

    @Column(name = "ALAM_ACKNOWLEDGED")
    private Date alamAcknowledged;

    @Column(name = "ALAM_USERACKNOWLEDGED")
    private String alamUserAcknowledged;

    @Column(name = "ALAM_PARENT_NE")
    private String alamParentNe;

    // Overriding clone() method of Object class
    public Alarms copy() throws CloneNotSupportedException {
        return (Alarms) super.clone();
    }
}
