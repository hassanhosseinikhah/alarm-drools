package com.example.alarmdrools.repository;

import com.example.alarmdrools.model.entity.AlarmComments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<AlarmComments, Long> {
}
