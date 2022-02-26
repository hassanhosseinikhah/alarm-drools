package com.example.alarmdrools.model.entity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by azar on 1/12/2018.
 */
@Getter
@Setter
@Entity
@Table(name = "Alarm_Comments")
public class AlarmComments {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "alac_seq")
    @SequenceGenerator(name = "alac_seq", allocationSize = 1, sequenceName = "ALAC_ID_SEQ")
    @Column(name = "ALAC_ID", nullable = false)
    private Long alacId;

    @Column(name = "ALAC_ALAM_ID")
    private Long alacAlamId;

    @Column(name = "ALAC_ALAS_OCCURRENCEID", nullable = false)
    private String alacAlasOccurrenceId;

    @Column(name = "ALAC_SEQUENCE")
    private Long alacSequence;

    @Column(name = "ALAC_USERNAME", nullable = false)
    private String alacUserName;

    @Column(name = "ALAC_TIMESTAMP", nullable = false)
    private Date alacTimeStamp;

    @Column(name = "ALAC_TEXT", nullable = false)
    private String alacText;

    @Column(name = "ALAC_USERENTERED", nullable = false)
    private String alacUserEntered;

    @Column(name = "ALAC_ALAC_ID")
    private Long alacAlacId;
}

