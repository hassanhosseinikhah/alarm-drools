package com.example.alarmdrools.repository;



import com.example.alarmdrools.model.dto.RuleAttributeId;
import com.example.alarmdrools.model.entity.RuleAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RuleAttributeRepository extends JpaRepository<RuleAttribute, RuleAttributeId> {

    List<RuleAttribute> findAllByCategory(String category);
}
