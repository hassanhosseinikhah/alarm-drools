package com.example.alarmdrools.repository;



import com.example.alarmdrools.model.dto.AlarmStatesID;
import com.example.alarmdrools.model.entity.AlarmStates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmStatesRepository extends JpaRepository<AlarmStates, AlarmStatesID> {
    @Query("select s from AlarmStates s where s.alasOccurrenceId in (select p.foreignId from ProblemLinks p where p.foreignType = 'ALARMS' and p.linkProblem.number = :promNumber )")
    List<AlarmStates> findStatesByFaultNumber(@Param("promNumber") Long promNumber);

    @Query("SELECT a from AlarmStates a where a.alasOccurrenceId = :alasOccurrenceId")
    AlarmStates getByAlasOccurrenceId(@Param("alasOccurrenceId")String alasOccurrenceId);
}