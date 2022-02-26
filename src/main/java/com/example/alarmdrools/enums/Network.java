package com.example.alarmdrools.enums;

public enum Network {
    G2G("2G"),G3G("3G"),G4G("4G");
    private final String name;
    Network(String name){
        this.name=name;
    }
    public String getName() {
        return name;
    }
}
