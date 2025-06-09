package com.gamecraft.studios.repository;

import com.gamecraft.studios.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    Optional<Skill> findByName(String name);

    List<Skill> findByCategory(Skill.Category category);

    List<Skill> findByNameContainingIgnoreCase(String name);

    @Query("SELECT s FROM Skill s WHERE s.category = :category ORDER BY s.name")
    List<Skill> findByCategoryOrderByName(@Param("category") Skill.Category category);

    // 인기 있는 기술 스택 (많이 사용되는 순)
    @Query("SELECT s, COUNT(jps) as usage_count FROM Skill s " +
            "LEFT JOIN s.jobPositionSkills jps " +
            "GROUP BY s ORDER BY usage_count DESC")
    List<Object[]> findPopularSkills();
}