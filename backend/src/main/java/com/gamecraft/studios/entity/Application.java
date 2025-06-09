package com.gamecraft.studios.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "applications", indexes = {
        @Index(name = "idx_application_user", columnList = "user_id"),
        @Index(name = "idx_application_company", columnList = "company"),
        @Index(name = "idx_application_status", columnList = "status")
})
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 지원자 (User와 연관관계)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 기본 정보
    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private String position;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExperienceLevel experienceLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobType jobType;

    // PostgreSQL JSON 타입 활용 (MySQL과 다른 점!)
    @Column(columnDefinition = "jsonb")
    private String skills; // JSON 형태: ["React", "Spring Boot", "PostgreSQL"]

    // 긴 텍스트는 TEXT 타입 사용
    @Column(columnDefinition = "TEXT")
    private String coverLetter;

    // 추가 정보
    private String expectedSalary;
    private String availableStartDate;
    private String workLocation;
    private String referenceLink;

    // 파일 업로드
    private String resumeFileName;
    private String resumeFilePath;
    private String portfolioFileName;
    private String portfolioFilePath;

    // 상태 관리
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.SUBMITTED;

    @Column(columnDefinition = "TEXT")
    private String adminNotes;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Enum 정의들
    public enum Status {
        SUBMITTED("제출완료"),
        REVIEWING("검토중"),
        INTERVIEW("면접진행"),
        ACCEPTED("합격"),
        REJECTED("불합격");

        private final String description;

        Status(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public enum ExperienceLevel {
        JUNIOR("신입"),
        MID("경력 3-5년"),
        SENIOR("경력 5년+"),
        LEAD("리드급");

        private final String description;

        ExperienceLevel(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public enum JobType {
        FRONTEND("프론트엔드"),
        BACKEND("백엔드"),
        FULLSTACK("풀스택"),
        MOBILE("모바일"),
        DEVOPS("데브옵스"),
        DATA("데이터");

        private final String description;

        JobType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    // 기본 생성자
    public Application() {}

    // 생성자
    public Application(User user, String company, String position) {
        this.user = user;
        this.company = company;
        this.position = position;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public ExperienceLevel getExperienceLevel() { return experienceLevel; }
    public void setExperienceLevel(ExperienceLevel experienceLevel) { this.experienceLevel = experienceLevel; }

    public JobType getJobType() { return jobType; }
    public void setJobType(JobType jobType) { this.jobType = jobType; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public String getCoverLetter() { return coverLetter; }
    public void setCoverLetter(String coverLetter) { this.coverLetter = coverLetter; }

    public String getExpectedSalary() { return expectedSalary; }
    public void setExpectedSalary(String expectedSalary) { this.expectedSalary = expectedSalary; }

    public String getAvailableStartDate() { return availableStartDate; }
    public void setAvailableStartDate(String availableStartDate) { this.availableStartDate = availableStartDate; }

    public String getWorkLocation() { return workLocation; }
    public void setWorkLocation(String workLocation) { this.workLocation = workLocation; }

    public String getReferenceLink() { return referenceLink; }
    public void setReferenceLink(String referenceLink) { this.referenceLink = referenceLink; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public String getAdminNotes() { return adminNotes; }
    public void setAdminNotes(String adminNotes) { this.adminNotes = adminNotes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}