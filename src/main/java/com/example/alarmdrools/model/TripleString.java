package com.example.alarmdrools.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TripleString {
    private String firstString;
    private String secondString;
    private String thirdString;

    public TripleString(String firstString, String secondString) {
        this.firstString = firstString;
        this.secondString = secondString;
    }
}
