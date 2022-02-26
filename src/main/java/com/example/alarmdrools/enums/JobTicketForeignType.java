package com.example.alarmdrools.enums;

public enum JobTicketForeignType {

    WOA("WOA"),
    WA("WO"),
    TASK("ACKNOWLEDGE"),
    FM("OPEN");

    private String type;

    JobTicketForeignType(final String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
