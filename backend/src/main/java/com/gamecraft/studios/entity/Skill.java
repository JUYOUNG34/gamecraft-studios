package com.gamecraft.studios.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "skills")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(length = 500)
    private String iconUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 연관관계
    @OneToMany(mappedBy = "skill", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<JobPositionSkill> jobPositionSkills;

    // 기본 생성자
    public Skill() {}

    // 생성자
    public Skill(String name, Category category) {
        this.name = name;
        this.category = category;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public String getIconUrl() { return iconUrl; }
    public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public List<JobPositionSkill> getJobPositionSkills() { return jobPositionSkills; }
    public void setJobPositionSkills(List<JobPositionSkill> jobPositionSkills) { this.jobPositionSkills = jobPositionSkills; }

    public enum Category {
        PROGRAMMING_LANGUAGE("프로그래밍 언어"),
        FRONTEND("프론트엔드"),
        BACKEND("백엔드"),
        DATABASE("데이터베이스"),
        DEVOPS("데브옵스"),
        CLOUD("클라우드"),
        MOBILE("모바일"),
        GAME("게임"),
        AI_ML("AI/ML"),
        TOOL("도구");

        private final String description;

        Category(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}