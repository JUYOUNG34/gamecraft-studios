package com.gamecraft.studios.service;

import com.gamecraft.studios.dto.GitHubApiDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GitHubTechStackService {

    private static final Logger logger = LoggerFactory.getLogger(GitHubTechStackService.class);

    private final RestTemplate restTemplate;

    @Value("${github.api.token:}")
    private String githubToken;

    // 주요 기술 스택 정의 (카카오게임즈 맞춤)
    private static final Map<String, String> TECH_CATEGORIES = Map.of(
            "Java", "BACKEND",
            "JavaScript", "FRONTEND",
            "TypeScript", "FRONTEND",
            "Python", "BACKEND",
            "Go", "BACKEND",
            "Kotlin", "BACKEND",
            "Swift", "MOBILE",
            "C#", "GAME"
    );

    private static final Map<String, String> FRAMEWORK_SEARCHES = Map.of(
            "Spring Boot", "spring-boot",
            "React", "react",
            "Vue.js", "vue",
            "Angular", "angular",
            "Express.js", "express",
            "Django", "django",
            "FastAPI", "fastapi",
            "Next.js", "nextjs"
    );

    public GitHubTechStackService() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * GitHub에서 인기 기술 스택 조회
     */
    public List<GitHubApiDto.TechStack> getTrendingTechStacks() {
        logger.info("GitHub API에서 트렌딩 기술 스택 조회 시작");

        List<GitHubApiDto.TechStack> allTechStacks = new ArrayList<>();

        // 1. 프로그래밍 언어별 인기도 조회
        allTechStacks.addAll(getLanguageStats());

        // 2. 프레임워크/라이브러리 인기도 조회
        allTechStacks.addAll(getFrameworkStats());

        // 3. 인기도 순으로 정렬
        return allTechStacks.stream()
                .sorted((a, b) -> Long.compare(b.getPopularityScore(), a.getPopularityScore()))
                .collect(Collectors.toList());
    }

    /**
     * 프로그래밍 언어 통계
     */
    private List<GitHubApiDto.TechStack> getLanguageStats() {
        List<GitHubApiDto.TechStack> languageStats = new ArrayList<>();

        for (Map.Entry<String, String> entry : TECH_CATEGORIES.entrySet()) {
            try {
                String language = entry.getKey();
                String category = entry.getValue();

                // GitHub에서 해당 언어의 인기 저장소 검색
                String searchQuery = String.format("language:%s stars:>1000", language.toLowerCase());
                GitHubApiDto.SearchResponse response = searchRepositories(searchQuery, 10);

                if (response != null && !response.getItems().isEmpty()) {
                    // 총 스타 수 계산
                    long totalStars = response.getItems().stream()
                            .mapToLong(GitHubApiDto.Repository::getStargazersCount)
                            .sum();

                    int repoCount = response.getTotalCount();

                    GitHubApiDto.TechStack techStack = new GitHubApiDto.TechStack(
                            language, category, totalStars, repoCount
                    );

                    // 설명 추가
                    String topRepo = response.getItems().get(0).getName();
                    techStack.setDescription(String.format("대표 프로젝트: %s (★%,d)",
                            topRepo, response.getItems().get(0).getStargazersCount()));

                    languageStats.add(techStack);

                    logger.info("언어 통계 수집: {} - 총 {}개 저장소, {}만 스타",
                            language, repoCount, totalStars / 10000);
                }

                // API 호출 제한 고려 (5000 requests/hour)
                Thread.sleep(200);

            } catch (Exception e) {
                logger.warn("언어 통계 수집 실패: " + entry.getKey(), e);
            }
        }

        return languageStats;
    }

    /**
     * 프레임워크/라이브러리 통계
     */
    private List<GitHubApiDto.TechStack> getFrameworkStats() {
        List<GitHubApiDto.TechStack> frameworkStats = new ArrayList<>();

        for (Map.Entry<String, String> entry : FRAMEWORK_SEARCHES.entrySet()) {
            try {
                String frameworkName = entry.getKey();
                String searchTerm = entry.getValue();

                // 프레임워크 관련 저장소 검색
                String searchQuery = String.format("%s stars:>500", searchTerm);
                GitHubApiDto.SearchResponse response = searchRepositories(searchQuery, 5);

                if (response != null && !response.getItems().isEmpty()) {
                    // 평균 스타 수 계산
                    double avgStars = response.getItems().stream()
                            .mapToLong(GitHubApiDto.Repository::getStargazersCount)
                            .average()
                            .orElse(0);

                    String category = categorizeFramework(frameworkName);

                    GitHubApiDto.TechStack techStack = new GitHubApiDto.TechStack(
                            frameworkName, category, (long) avgStars, response.getTotalCount()
                    );

                    // 설명 추가
                    String description = String.format("평균 ★%.0f, %,d+ 프로젝트",
                            avgStars, response.getTotalCount());
                    techStack.setDescription(description);

                    frameworkStats.add(techStack);

                    logger.info("프레임워크 통계 수집: {} - 평균 {:.0f} 스타",
                            frameworkName, avgStars);
                }

                Thread.sleep(200);

            } catch (Exception e) {
                logger.warn("프레임워크 통계 수집 실패: " + entry.getKey(), e);
            }
        }

        return frameworkStats;
    }

    /**
     * GitHub API 저장소 검색
     */
    private GitHubApiDto.SearchResponse searchRepositories(String query, int perPage) {
        try {
            String url = String.format(
                    "https://api.github.com/search/repositories?q=%s&sort=stars&order=desc&per_page=%d",
                    query, perPage
            );

            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/vnd.github.v3+json");
            headers.set("User-Agent", "GameCraft-Studios");

            // GitHub Token이 있으면 사용 (API 제한 증가)
            if (githubToken != null && !githubToken.trim().isEmpty()) {
                headers.set("Authorization", "token " + githubToken);
            }

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<GitHubApiDto.SearchResponse> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, GitHubApiDto.SearchResponse.class
            );

            return response.getBody();

        } catch (Exception e) {
            logger.error("GitHub API 호출 실패: " + query, e);
            return null;
        }
    }

    /**
     * 프레임워크 카테고리 분류
     */
    private String categorizeFramework(String framework) {
        if (framework.contains("Spring")) return "BACKEND";
        if (Arrays.asList("React", "Vue.js", "Angular", "Next.js").contains(framework)) return "FRONTEND";
        if (Arrays.asList("Express.js", "Django", "FastAPI").contains(framework)) return "BACKEND";
        return "FRAMEWORK";
    }

    /**
     * 카카오게임즈 맞춤 기술 스택 추천
     */
    public List<GitHubApiDto.TechStack> getGameIndustryRecommendedStacks() {
        List<GitHubApiDto.TechStack> gameStacks = new ArrayList<>();

        // 게임 업계 핵심 기술들 GitHub에서 검색
        Map<String, String> gameKeywords = Map.of(
                "게임 서버", "game server java",
                "실시간 통신", "realtime websocket",
                "대용량 처리", "high performance java",
                "Redis 캐싱", "redis cache",
                "Unity 연동", "unity backend"
        );

        for (Map.Entry<String, String> entry : gameKeywords.entrySet()) {
            try {
                GitHubApiDto.SearchResponse response = searchRepositories(entry.getValue(), 3);

                if (response != null && !response.getItems().isEmpty()) {
                    long avgStars = (long) response.getItems().stream()
                            .mapToLong(GitHubApiDto.Repository::getStargazersCount)
                            .average()
                            .orElse(0);

                    GitHubApiDto.TechStack techStack = new GitHubApiDto.TechStack(
                            entry.getKey(), "GAME_TECH", avgStars, response.getTotalCount()
                    );

                    techStack.setDescription("게임 업계 추천 기술");
                    gameStacks.add(techStack);
                }

                Thread.sleep(200);

            } catch (Exception e) {
                logger.warn("게임 기술 검색 실패: " + entry.getKey(), e);
            }
        }

        return gameStacks;
    }
}