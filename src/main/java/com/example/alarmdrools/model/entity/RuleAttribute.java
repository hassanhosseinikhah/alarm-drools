package com.example.alarmdrools.model.entity;

import com.example.alarmdrools.enums.AttributeMethod;
import com.example.alarmdrools.model.dto.RuleAttributeId;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "SIMPLE_RULE_ATTRIBUTES")
@NoArgsConstructor
@IdClass(RuleAttributeId.class)
public class RuleAttribute {

    @Id
    @Column(name = "SRAT_CATEGORY")
    private String category;

    @Id
    @Column(name = "SRAT_PRAL_NAME")
    private String name;

    @Column(name = "SRAT_VALUE")
    private String value;

    @Column(name = "SRAT_METHOD", length = 20)
    @Enumerated(value = EnumType.STRING)
    private AttributeMethod method;
}
