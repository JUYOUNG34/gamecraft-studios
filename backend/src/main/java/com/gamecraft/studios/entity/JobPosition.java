package com.gamecraft.studios.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "job_positions")
public class JobPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 회사 정보
    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private String companyLogo; // 회사 로고 URL

    private String companyDescription;

    // 포지션 정보
    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String requirements;

    @Column(columnDefinition = "TEXT")
    private String preferredQualifications;

    // 고용 정보
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobType jobType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExperienceLevel experienceLevel;

    private String salaryRange; // 예: "4000-6000만원"

    // 근무 조건
    @Column(nullable = false)
    private String location;

    private Boolean remoteWorkAvailable = false;

    // 기술 스택 (JSON 형태로 저장)
    @Column(columnDefinition = "TEXT")
    private String requiredSkills; // ["React", "TypeScript", "Node.js"]

    @Column(columnDefinition = "TEXT")
    private String preferredSkills; // ["AWS", "Docker", "Kubernetes"]

    // 혜택 및 복리후생
    @Column(columnDefinition = "TEXT")
    private String benefits; // ["4대보험", "연차", "교육비 지원"]

    // 상태 관리
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ACTIVE;

    private LocalDateTime applicationDeadline;

    // SEO 및 추가 정보
    private String slug; // URL용 슬러그
    private Integer viewCount = 0;
    private Integer applicationCount = 0;

    // 담당자 정보
    private String contactEmail;
    private String contactPerson;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // 연관관계 - 이 포지션에 대한 지원서들
    @OneToMany(mappedBy = "jobPosition", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Application> applications;

    // 기본 생성자
    public JobPosition() {}

    // 생성자
    public JobPosition(String company, String title, String description,
                       JobType jobType, ExperienceLevel experienceLevel, String location) {
        this.company = company;
        this.title = title;
        this.description = description;
        this.jobType = jobType;
        this.experienceLevel = experienceLevel;
        this.location = location;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }

    public String getCompanyDescription() {
        return companyDescription;
    }

    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getPreferredQualifications() {
        return preferredQualifications;
    }

    public void setPreferredQualifications(String preferredQualifications) {
        this.preferredQualifications = preferredQualifications;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    public ExperienceLevel getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(ExperienceLevel experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public String getSalaryRange() {
        return salaryRange;
    }

    public void setSalaryRange(String salaryRange) {
        this.salaryRange = salaryRange;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getRemoteWorkAvailable() {
        return remoteWorkAvailable;
    }

    public void setRemoteWorkAvailable(Boolean remoteWorkAvailable) {
        this.remoteWorkAvailable = remoteWorkAvailable;
    }

    public String getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(String requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    public String getPreferredSkills() {
        return preferredSkills;
    }

    public void setPreferredSkills(String preferredSkills) {
        this.preferredSkills = preferredSkills;
    }

    public String getBenefits() {
        return benefits;
    }

    public void setBenefits(String benefits) {
        this.benefits = benefits;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getApplicationDeadline() {
        return applicationDeadline;
    }

    public void setApplicationDeadline(LocalDateTime applicationDeadline) {
        this.applicationDeadline = applicationDeadline;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getApplicationCount() {
        return applicationCount;
    }

    public void setApplicationCount(Integer applicationCount) {
        this.applicationCount = applicationCount;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
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

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

    // 열거형들
    public enum JobType {
        FULL_TIME("정규직"),
        CONTRACT("계약직"),
        FREELANCE("프리랜서"),
        INTERN("인턴"),
        PART_TIME("파트타임");

        private final String description;

        JobType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public enum ExperienceLevel {
        JUNIOR("신입"),
        MIDDLE("경력 3-5년"),
        SENIOR("경력 5년 이상"),
        LEAD("팀 리드"),
        EXECUTIVE("임원");

        private final String description;

        ExperienceLevel(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public enum Status {
        ACTIVE("채용중"),
        CLOSED("채용완료"),
        SUSPENDED("일시중단"),
        DRAFT("임시저장");

        private final String description;

        Status(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    // 비즈니스 메서드
    public void incrementViewCount() {
        this.viewCount = (this.viewCount == null ? 0 : this.viewCount) + 1;
    }

    public void incrementApplicationCount() {
        this.applicationCount = (this.applicationCount == null ? 0 : this.applicationCount) + 1;
    }

    public boolean isApplicationDeadlinePassed() {
        return applicationDeadline != null && LocalDateTime.now().isAfter(applicationDeadline);
    }

    public boolean isActive() {
        return status == Status.ACTIVE && !isApplicationDeadlinePassed();
    }
}