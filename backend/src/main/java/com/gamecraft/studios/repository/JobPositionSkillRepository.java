package com.gamecraft.studios.repository;

import com.gamecraft.studios.entity.JobPositionSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPositionSkillRepository extends JpaRepository<JobPositionSkill, Long> {

    List<JobPositionSkill> findByJobPositionId(Long jobPositionId);

    List<JobPositionSkill> findBySkillId(Long skillId);

    @Query("SELECT jps FROM JobPositionSkill jps WHERE jps.jobPosition.id = :jobPositionId AND jps.isRequired = true")
    List<JobPositionSkill> findRequiredSkillsByJobPosition(@Param("jobPositionId") Long jobPositionId);

    @Query("SELECT jps FROM JobPositionSkill jps WHERE jps.jobPosition.id = :jobPositionId AND jps.isRequired = false")
    List<JobPositionSkill> findPreferredSkillsByJobPosition(@Param("jobPositionId") Long jobPositionId);
}