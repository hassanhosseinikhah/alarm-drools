package com.example.alarmdrools.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "PROBLEM_LINKS")
@IdClass(ProblemLinksID.class)
@AllArgsConstructor

public class ProblemLinks {
//
//    @JoinColumn(name = "PROL_PROM_NUMBER", insertable = false, updatable = false)
//    @JsonManagedReference
//    @ManyToOne
//    private Problems linkProblem;

    @Id
    @Column(name = "PROL_PROM_NUMBER")
    private Long id;
    @Id
    @Column(name = "PROL_FOREIGNID", length = 55)
    private String foreignId;
    @Id
    @Column(name = "PROL_FOREIGNTYPE", length = 40)
    private String foreignType;
    @Column(name = "PROL_FOREIGNPARENT", length = 55)
    private String foreignParent;
//
//    public ProblemLinks(long ticketNumber, CreateProblemLink linkDTO) {
//        this.id = ticketNumber;
//        this.foreignId = linkDTO.getForeignId();
//        this.foreignType = linkDTO.getForeignType();
//        this.foreignParent = linkDTO.getForeignParent();
//    }


    public ProblemLinks() {
    }

    public ProblemLinks(String foreignId, String foreignType) {
        this.foreignId = foreignId;
        this.foreignType = foreignType;
    }
}
