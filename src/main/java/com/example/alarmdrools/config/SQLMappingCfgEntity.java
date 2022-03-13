package com.example.alarmdrools.config;

import com.example.alarmdrools.model.TripleString;
import com.example.alarmdrools.model.dto.*;

import javax.persistence.*;
import javax.xml.crypto.Data;
import java.util.Date;

@SqlResultSetMappings({
        @SqlResultSetMapping(name = "AlarmMapping",
                classes = {
                        @ConstructorResult(
                                targetClass = Alarm.class,
                                columns = {
                                        @ColumnResult(name = "ALAM_ID", type = Long.class),
                                        @ColumnResult(name = "ALAM_LOCN_TTNAME"),
                                        @ColumnResult(name = "ALAM_EQUT_ABBREVIATION"),
                                        @ColumnResult(name = "ALAM_EQUP_INDEX"),
                                        @ColumnResult(name = "ALAM_PORT"),
                                        @ColumnResult(name = "ALAM_ALMT_ABBREVIATION"),
                                        @ColumnResult(name = "ALAM_MANS_NAME"),
                                        @ColumnResult(name = "ALAM_PROCESSINGTYPE", type = Long.class),
                                        @ColumnResult(name = "ALAM_DMATIMESTAMP", type = Data.class),
                                        @ColumnResult(name = "ALAM_ALARMTYPE"),
                                        @ColumnResult(name = "ALAM_SUBEQP"),
                                        @ColumnResult(name = "ALAM_PORT_NAME"),
                                        @ColumnResult(name = "ALAM_PORTTYPE"),
                                        @ColumnResult(name = "ALAM_ACTIVE"),
                                        @ColumnResult(name = "ALAM_ALAS_OCCURRENCEID"),
                                        @ColumnResult(name = "ALAM_PROBABLECAUSE"),
                                        @ColumnResult(name = "ALAM_EVENTTYPE"),
                                        @ColumnResult(name = "ALAM_OBJECTCLASS"),
                                        @ColumnResult(name = "ALAM_AREA_CODE"),
                                        @ColumnResult(name = "ALAM_REPORTED", type = Data.class),
                                        @ColumnResult(name = "ALAM_SEVERITY"),
                                        @ColumnResult(name = "ALAM_ACKNOWLEDGED", type = Data.class),
                                        @ColumnResult(name = "ALAM_USERACKNOWLEDGED"),
                                        @ColumnResult(name = "ALAM_PARENT_NE"),
                                        @ColumnResult(name = "ALAS_INFO"),
                                        @ColumnResult(name = "ALAS_OCCURRENCEID"),
                                        @ColumnResult(name = "ALAS_ALAM_ID", type = Long.class),
                                        @ColumnResult(name = "ALAS_REPORTED", type = Date.class),
                                        @ColumnResult(name = "ALAS_ACKNOWLEDGED", type = Date.class),
                                        @ColumnResult(name = "ALAS_USERCLEAREDTIME", type = Date.class),
                                        @ColumnResult(name = "ALAS_CLEARED", type = Date.class),
                                        @ColumnResult(name = "ALAS_USERACKNOWLEDGED"),
                                        @ColumnResult(name = "ALAS_USERCLEARED"),
                                        @ColumnResult(name = "ALAS_PRIORITY", type = Long.class),
                                        @ColumnResult(name = "ALAS_SEVERITY"),
                                        @ColumnResult(name = "ALAS_MESSAGE"),
                                        @ColumnResult(name = "ALAS_TRIGGERINGALARM"),
                                        @ColumnResult(name = "ALAS_PROBABLEROOT"),
                                        @ColumnResult(name = "ALAS_CORRELATIONID"),
                                        @ColumnResult(name = "ALAS_ROOTCAUSEID"),
                                        @ColumnResult(name = "ALAS_OBJECTID"),
                                        @ColumnResult(name = "ALAS_ALARMID"),
                                        @ColumnResult(name = "ALAS_SERT_ABBREVIATION"),
                                        @ColumnResult(name = "ALAS_CIRT_DISPLAYNAME"),
                                        @ColumnResult(name = "ALAS_SUPPRESSED_YN"),
                                        @ColumnResult(name = "ALAS_ALTH_NAME"),
                                        @ColumnResult(name = "ALAS_TIMESTAMP", type = Date.class),
                                        //@ColumnResult(name = "AREA_CODE"),
                                }),
                }
        ),
        @SqlResultSetMapping(name = "DualStringMapping",
                classes = {
                        @ConstructorResult(
                                targetClass = TripleString.class,
                                columns = {
                                        @ColumnResult(name = "C1"),
                                        @ColumnResult(name = "C2"),
                                }),
                }
        ),
        @SqlResultSetMapping(name = "TripleStringMapping",
                classes = {
                        @ConstructorResult(
                                targetClass = TripleString.class,
                                columns = {
                                        @ColumnResult(name = "C1"),
                                        @ColumnResult(name = "C2"),
                                        @ColumnResult(name = "C3"),
                                }),
                }
        ),
        @SqlResultSetMapping(name = "ProblemMapping",
                classes = {
                        @ConstructorResult(
                                targetClass = ProblemDTO.class,
                                columns = {
                                        @ColumnResult(name = "PROM_NUMBER",type = Long.class),
                                        @ColumnResult(name = "PROM_CREATED",type = Data.class),
                                        @ColumnResult(name = "PROM_CAUSE"),
                                        @ColumnResult(name = "PROM_REPORTED"),
                                        @ColumnResult(name = "ALAM_ALARMTYPE",type = Data.class),
                                        @ColumnResult(name = "PRAT_VALUE"),
                                }),
                }
        ),
        @SqlResultSetMapping(name = "ProblemStatusMapping",
                classes = {
                        @ConstructorResult(
                                targetClass = ProblemStatus.class,
                                columns = {
                                        @ColumnResult(name = "PROM_NUMBER",type = Long.class),
                                        @ColumnResult(name = "PROM_PROS_CODE",type = Long.class),

                                }),
                }
        ),
        @SqlResultSetMapping(name = "AttributesValue",
                classes = {
                        @ConstructorResult(
                                targetClass = Attributes.class,
                                columns = {
                                        @ColumnResult(name = "PRAT_VALUE"),



                                }),
                }
        ),


})
@Entity
class SQLMappingCfgEntity {
    @Id
    private String dummy;
}
