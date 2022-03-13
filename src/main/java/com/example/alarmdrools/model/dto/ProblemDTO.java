package com.example.alarmdrools.model.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import org.springframework.data.redis.core.RedisHash;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@RedisHash("ProblemDTO")
public class ProblemDTO  implements Serializable {
    private static final long serialVersionUID = 1765349294017032147L;

    private Long PROM_NUMBER;
    private Date    PROM_CREATED;
    private String PROM_CAUSE;
    private Date PROM_REPORTED;
    private String ALAM_ALARMTYPE;
    private String PRAT_VALUE;


}
