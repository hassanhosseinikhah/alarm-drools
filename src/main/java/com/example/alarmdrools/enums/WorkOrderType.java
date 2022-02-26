package com.example.alarmdrools.enums;

public enum WorkOrderType {
    M("M"),
    A("A");

    String value;

    private WorkOrderType(String value) {
        this.value = value;
    }
}