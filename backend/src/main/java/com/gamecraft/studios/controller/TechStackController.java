package com.gamecraft.studios.controller;

import com.gamecraft.studios.dto.GitHubApiDto;
import com.gamecraft.studios.service.GitHubTechStackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tech-stacks")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class TechStackController {

    @Autowired
    private GitHubTechStackService gitHubTechStackService;

    /**
     * GitHub 기반 실시간 트렌딩 기술 스택
     */
    @GetMapping("/trending")
    public ResponseEntity<Map<String, Object>> getTrendingTechStacks() {
        Map<String, Object> response = new HashMap<>();

        try {
            List<GitHubApiDto.TechStack> trendingStacks = gitHubTechStackService.getTrendingTechStacks();

            // 카테고리별로 그룹핑
            Map<String, List<GitHubApiDto.TechStack>> groupedStacks = trendingStacks.stream()
                    .collect(Collectors.groupingBy(GitHubApiDto.TechStack::getCategory));

            response.put("success", true);
            response.put("data", groupedStacks);
            response.put("flatData", trendingStacks); // 프론트엔드에서 쉽게 사용
            response.put("totalCount", trendingStacks.size());
            response.put("lastUpdated", LocalDateTime.now());
            response.put("source", "GitHub API 실시간 데이터");
            response.put("description", "GitHub ⭐ 수 기반 인기 기술 스택");

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "GitHub API 조회 중 오류가 발생했습니다: " + e.getMessage());
            response.put("fallbackData", getFallbackTechStacks()); // 장애 시 기본 데이터
            return ResponseEntity.ok(response); // 500 대신 200으로 반환
        }

        return ResponseEntity.ok(response);
    }

    /**
     * 카카오게임즈 맞춤 기술 스택 추천
     */
    @GetMapping("/kakao-games-recommended")
    public ResponseEntity<Map<String, Object>> getKakaoGamesRecommended() {
        Map<String, Object> response = new HashMap<>();

        try {
            List<GitHubApiDto.TechStack> gameStacks = gitHubTechStackService.getGameIndustryRecommendedStacks();
            List<GitHubApiDto.TechStack> generalStacks = gitHubTechStackService.getTrendingTechStacks();

            // 게임업계 필수 + 일반 인기 기술 조합
            List<GitHubApiDto.TechStack> recommendedStacks = gameStacks.stream()
                    .limit(10)
                    .collect(Collectors.toList());

            // 일반 트렌딩에서 상위 기술도 추가
            generalStacks.stream()
                    .filter(stack -> isRelevantForGaming(stack.getName()))
                    .limit(15)
                    .forEach(recommendedStacks::add);

            response.put("success", true);
            response.put("data", recommendedStacks);
            response.put("description", "카카오게임즈 회원플랫폼 개발자 추천 기술");
            response.put("categories", Map.of(
                    "핵심기술", "Java, Spring Boot, PostgreSQL, Redis",
                    "클라우드", "AWS, Docker, Kubernetes",
                    "프론트엔드", "React, TypeScript, Next.js",
                    "게임특화", "실시간 통신, 대용량 처리, 캐싱"
            ));

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "추천 스택 조회 오류: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    /**
     * 간단한 기술 스택 목록 (지원서 작성용)
     */
    @GetMapping("/simple-list")
    public ResponseEntity<Map<String, Object>> getSimpleTechList() {
        Map<String, Object> response = new HashMap<>();

        try {
            List<GitHubApiDto.TechStack> trendingStacks = gitHubTechStackService.getTrendingTechStacks();

            // 지원서에서 선택하기 쉽게 이름만 추출
            List<String> techNames = trendingStacks.stream()
                    .map(GitHubApiDto.TechStack::getName)
                    .limit(30) // 상위 30개만
                    .collect(Collectors.toList());

            // 카테고리별로 분류
            Map<String, List<String>> categorizedTechs = new HashMap<>();
            categorizedTechs.put("BACKEND", List.of("Java", "Spring Boot", "Node.js", "Python", "PostgreSQL", "Redis"));
            categorizedTechs.put("FRONTEND", List.of("React", "Vue.js", "TypeScript", "Next.js", "Angular"));
            categorizedTechs.put("DEVOPS", List.of("Docker", "Kubernetes", "AWS", "Jenkins", "Git"));
            categorizedTechs.put("MOBILE", List.of("React Native", "Flutter", "Swift", "Kotlin"));

            response.put("success", true);
            response.put("techNames", techNames);
            response.put("categorized", categorizedTechs);
            response.put("source", "GitHub 트렌딩 데이터 기반");

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "기술 목록 조회 오류: " + e.getMessage());
            response.put("fallbackTechs", getFallbackTechNames());
        }

        return ResponseEntity.ok(response);
    }

    // 헬퍼 메서드들
    private boolean isRelevantForGaming(String techName) {
        List<String> relevantTechs = List.of(
                "Java", "Spring Boot", "Redis", "PostgreSQL", "TypeScript",
                "React", "AWS", "Docker", "Kubernetes", "JavaScript"
        );
        return relevantTechs.contains(techName);
    }

    private List<String> getFallbackTechNames() {
        return List.of(
                "Java", "Spring Boot", "React", "TypeScript", "PostgreSQL",
                "Redis", "AWS", "Docker", "Git", "JavaScript"
        );
    }

    private List<GitHubApiDto.TechStack> getFallbackTechStacks() {
        return List.of(
                new GitHubApiDto.TechStack("Java", "BACKEND", 50000, 1000),
                new GitHubApiDto.TechStack("Spring Boot", "BACKEND", 40000, 800),
                new GitHubApiDto.TechStack("React", "FRONTEND", 45000, 900),
                new GitHubApiDto.TechStack("TypeScript", "FRONTEND", 35000, 700)
        );
    }
}