package com.example.alarmdrools.repository;

import com.example.alarmdrools.model.entity.Alarms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface AlarmsRepositoryPrimary extends JpaRepository<Alarms, Long> {

    Alarms getByAlamAlasOccurrenceId(String s);
    @Transactional
    @Modifying
    @Query(value = "update Alarms a set a.alamUserAcknowledged=:userAcknowledged , a.alamAcknowledged=:acknowledged" +
            " where a.alamActive IN ('Y','N') and a.alamAlasOccurrenceId in :occurrenceIds ")
    void acknowledgeAlarms(String userAcknowledged, Date acknowledged, List<String> occurrenceIds);


}
