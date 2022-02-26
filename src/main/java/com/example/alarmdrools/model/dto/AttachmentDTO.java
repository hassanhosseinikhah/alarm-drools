package com.example.alarmdrools.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Azar Amiri
 * 07 May 2019 - 09:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentDTO {
    private Long id;
    private String fileName;
    private String type;
    private Long size;
    private Date dateAttached;
    private String userName;
}
