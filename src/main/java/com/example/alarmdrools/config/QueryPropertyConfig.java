package com.example.alarmdrools.config;



import java.io.*;
import java.util.Properties;

public class QueryPropertyConfig {


    private static Properties properties;

    static {
         properties = new Properties();
        try {
            properties.load(QueryPropertyConfig.class.getClassLoader().getResourceAsStream("queries.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private QueryPropertyConfig(){
    }

    public static Properties getProperties(){
        return properties;
    }



}
