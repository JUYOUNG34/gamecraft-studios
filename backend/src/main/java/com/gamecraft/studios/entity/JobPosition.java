package com.gamecraft.studios.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "job_positions", indexes = {
        @Index(name = "idx_job_company", columnList = "company"),
        @Index(name = "idx_job_status", columnList = "status"),
        @Index(name = "idx_job_location", columnList = "location")
})
public class JobPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExperienceLevel experienceLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobType jobType;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String requirements;

    @Column(columnDefinition = "TEXT")
    private String benefits;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ACTIVE;

    @Column(name = "view_count")
    private Long viewCount = 0L;

    @Column(name = "application_count")
    private Long applicationCount = 0L;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Enum 정의들
    public enum Status {
        ACTIVE, CLOSED, DRAFT
    }

    public enum ExperienceLevel {
        JUNIOR("신입"),
        MID("경력 3-5년"),
        SENIOR("경력 5년+"),
        LEAD("리드급");

        private final String description;
        ExperienceLevel(String description) { this.description = description; }
        public String getDescription() { return description; }
    }

    public enum JobType {
        FRONTEND("프론트엔드"),
        BACKEND("백엔드"),
        FULLSTACK("풀스택"),
        MOBILE("모바일"),
        GAME("게임개발"),
        DEVOPS("데브옵스"),
        DATA("데이터");

        private final String description;
        JobType(String description) { this.description = description; }
        public String getDescription() { return description; }
    }

    // 기본 생성자
    public JobPosition() {}

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public ExperienceLevel getExperienceLevel() { return experienceLevel; }
    public void setExperienceLevel(ExperienceLevel experienceLevel) { this.experienceLevel = experienceLevel; }

    public JobType getJobType() { return jobType; }
    public void setJobType(JobType jobType) { this.jobType = jobType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getRequirements() { return requirements; }
    public void setRequirements(String requirements) { this.requirements = requirements; }

    public String getBenefits() { return benefits; }
    public void setBenefits(String benefits) { this.benefits = benefits; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public Long getViewCount() { return viewCount; }
    public void setViewCount(Long viewCount) { this.viewCount = viewCount; }

    public Long getApplicationCount() { return applicationCount; }
    public void setApplicationCount(Long applicationCount) { this.applicationCount = applicationCount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}