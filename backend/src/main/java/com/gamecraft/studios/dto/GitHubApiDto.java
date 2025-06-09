package com.gamecraft.studios.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

// GitHub API 응답 DTO들
public class GitHubApiDto {

    // GitHub 저장소 검색 응답
    public static class SearchResponse {
        @JsonProperty("total_count")
        private int totalCount;

        @JsonProperty("items")
        private List<Repository> items;

        // Getters & Setters
        public int getTotalCount() { return totalCount; }
        public void setTotalCount(int totalCount) { this.totalCount = totalCount; }

        public List<Repository> getItems() { return items; }
        public void setItems(List<Repository> items) { this.items = items; }
    }

    // GitHub 저장소 정보
    public static class Repository {
        private String name;
        private String language;

        @JsonProperty("stargazers_count")
        private int stargazersCount;

        @JsonProperty("forks_count")
        private int forksCount;

        @JsonProperty("updated_at")
        private String updatedAt;

        private String description;

        @JsonProperty("html_url")
        private String htmlUrl;

        // Getters & Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getLanguage() { return language; }
        public void setLanguage(String language) { this.language = language; }

        public int getStargazersCount() { return stargazersCount; }
        public void setStargazersCount(int stargazersCount) { this.stargazersCount = stargazersCount; }

        public int getForksCount() { return forksCount; }
        public void setForksCount(int forksCount) { this.forksCount = forksCount; }

        public String getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getHtmlUrl() { return htmlUrl; }
        public void setHtmlUrl(String htmlUrl) { this.htmlUrl = htmlUrl; }
    }

    // 기술 스택 정보 (우리가 사용할 형태)
    public static class TechStack {
        private String name;
        private String category;
        private long popularityScore;
        private int repositoryCount;
        private String description;

        public TechStack(String name, String category, long popularityScore, int repositoryCount) {
            this.name = name;
            this.category = category;
            this.popularityScore = popularityScore;
            this.repositoryCount = repositoryCount;
        }

        // Getters & Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }

        public long getPopularityScore() { return popularityScore; }
        public void setPopularityScore(long popularityScore) { this.popularityScore = popularityScore; }

        public int getRepositoryCount() { return repositoryCount; }
        public void setRepositoryCount(int repositoryCount) { this.repositoryCount = repositoryCount; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
}