package com.clarity.alarmaction.action;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActionResult {
    private String message;

//    public ActionResult(String message) {
//        this.message = message;
//    }
}