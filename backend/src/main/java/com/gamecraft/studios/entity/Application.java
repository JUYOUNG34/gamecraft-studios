package com.gamecraft.studios.entity;

import jakarta.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "applications")
public class Application {

    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 연관관계 - 지원자
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

    // 기술 스택 (JSON 형태로 저장)
    @Column(columnDefinition = "TEXT")
    private String skills; // ["React", "Spring Boot", "PostgreSQL"]

    // 자기소개서
    @Column(columnDefinition = "TEXT")
    private String coverLetter;

    // 추가 정보
    private String expectedSalary;
    private String availableStartDate;
    private String workLocation;
    private String referenceLink;

    // 첨부 파일
    private String resumeFileName;
    private String resumeFilePath;
    private String portfolioFileName;
    private String portfolioFilePath;

    // 지원서 상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.SUBMITTED;

    // 관리자 메모
    @Column(columnDefinition = "TEXT")
    private String adminNotes;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // 기본 생성자
    public Application() {}

    // 생성자
    public Application(User user, String company, String position,
                       ExperienceLevel experienceLevel, JobType jobType,
                       String coverLetter) {
        this.user = user;
        this.company = company;
        this.position = position;
        this.experienceLevel = experienceLevel;
        this.jobType = jobType;
        this.coverLetter = coverLetter;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public ExperienceLevel getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(ExperienceLevel experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    public String getExpectedSalary() {
        return expectedSalary;
    }

    public void setExpectedSalary(String expectedSalary) {
        this.expectedSalary = expectedSalary;
    }

    public String getAvailableStartDate() {
        return availableStartDate;
    }

    public void setAvailableStartDate(String availableStartDate) {
        this.availableStartDate = availableStartDate;
    }

    public String getWorkLocation() {
        return workLocation;
    }

    public void setWorkLocation(String workLocation) {
        this.workLocation = workLocation;
    }

    public String getReferenceLink() {
        return referenceLink;
    }

    public void setReferenceLink(String referenceLink) {
        this.referenceLink = referenceLink;
    }

    public String getResumeFileName() {
        return resumeFileName;
    }

    public void setResumeFileName(String resumeFileName) {
        this.resumeFileName = resumeFileName;
    }

    public String getResumeFilePath() {
        return resumeFilePath;
    }

    public void setResumeFilePath(String resumeFilePath) {
        this.resumeFilePath = resumeFilePath;
    }

    public String getPortfolioFileName() {
        return portfolioFileName;
    }

    public void setPortfolioFileName(String portfolioFileName) {
        this.portfolioFileName = portfolioFileName;
    }

    public String getPortfolioFilePath() {
        return portfolioFilePath;
    }

    public void setPortfolioFilePath(String portfolioFilePath) {
        this.portfolioFilePath = portfolioFilePath;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getAdminNotes() {
        return adminNotes;
    }

    public void setAdminNotes(String adminNotes) {
        this.adminNotes = adminNotes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // 열거형들
    public enum ExperienceLevel {
        JUNIOR("신입"),
        MIDDLE("경력 3-5년"),
        SENIOR("경력 5년 이상"),
        LEAD("팀 리드");

        private final String description;

        ExperienceLevel(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public enum JobType {
        FULL_TIME("정규직"),
        CONTRACT("계약직"),
        FREELANCE("프리랜서"),
        INTERN("인턴");

        private final String description;

        JobType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public enum Status {
        SUBMITTED("지원 완료"),
        REVIEWING("검토중"),
        INTERVIEW_SCHEDULED("면접 예정"),
        INTERVIEW_COMPLETED("면접 완료"),
        ACCEPTED("합격"),
        REJECTED("불합격"),
        WITHDRAWN("지원 취소");

        private final String description;

        Status(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}