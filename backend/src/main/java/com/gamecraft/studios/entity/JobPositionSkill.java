package com.gamecraft.studios.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "job_position_skills")
public class JobPositionSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_position_id", nullable = false)
    private JobPosition jobPosition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Column(nullable = false)
    private Boolean isRequired = true; // 필수 기술 여부

    private Integer proficiencyLevel; // 숙련도 레벨 (1-5)

    // 기본 생성자
    public JobPositionSkill() {}

    // 생성자
    public JobPositionSkill(JobPosition jobPosition, Skill skill, Boolean isRequired) {
        this.jobPosition = jobPosition;
        this.skill = skill;
        this.isRequired = isRequired;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public JobPosition getJobPosition() { return jobPosition; }
    public void setJobPosition(JobPosition jobPosition) { this.jobPosition = jobPosition; }

    public Skill getSkill() { return skill; }
    public void setSkill(Skill skill) { this.skill = skill; }

    public Boolean getIsRequired() { return isRequired; }
    public void setIsRequired(Boolean isRequired) { this.isRequired = isRequired; }

    public Integer getProficiencyLevel() { return proficiencyLevel; }
    public void setProficiencyLevel(Integer proficiencyLevel) { this.proficiencyLevel = proficiencyLevel; }
}
