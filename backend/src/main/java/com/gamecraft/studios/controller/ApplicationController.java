package com.gamecraft.studios.controller;

import com.gamecraft.studios.entity.Application;
import com.gamecraft.studios.entity.User;
import com.gamecraft.studios.repository.ApplicationRepository;
import com.gamecraft.studios.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/application")
public class ApplicationController {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 지원서 작성 폼 정보 제공
     */
    @GetMapping("/form-info")
    public ResponseEntity<Map<String, Object>> getFormInfo() {
        Map<String, Object> response = new HashMap<>();

        response.put("companies", List.of(
                "카카오게임즈", "넥슨", "엔씨소프트", "넷마블", "컴투스",
                "스마일게이트", "펄어비스", "크래프톤", "기타"
        ));

        response.put("positions", List.of(
                "백엔드 개발자", "프론트엔드 개발자", "풀스택 개발자",
                "게임 서버 개발자", "모바일 개발자", "DevOps 엔지니어"
        ));

        response.put("experienceLevels", Map.of(
                "JUNIOR", "신입",
                "MIDDLE", "경력 3-5년",
                "SENIOR", "경력 5년 이상",
                "LEAD", "리드/매니저"
        ));

        response.put("jobTypes", Map.of(
                "FULL_TIME", "정규직",
                "CONTRACT", "계약직",
                "INTERN", "인턴"
        ));

        return ResponseEntity.ok(response);
    }

    /**
     * 지원서 작성
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createApplication(
            @AuthenticationPrincipal OAuth2User oauth2User,
            @RequestBody Map<String, Object> requestData) {

        Map<String, Object> response = new HashMap<>();

        if (oauth2User == null) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        try {
            // 현재 사용자 찾기
            Long kakaoId = Long.valueOf(oauth2User.getAttribute("id").toString());
            Optional<User> userOpt = userRepository.findByKakaoId(String.valueOf(kakaoId));

            if (!userOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "사용자 정보를 찾을 수 없습니다");
                return ResponseEntity.badRequest().body(response);
            }

            User user = userOpt.get();

            // 새 지원서 생성
            Application application = new Application();
            application.setUser(user);
            application.setCompany((String) requestData.get("company"));
            application.setPosition((String) requestData.get("position"));
            application.setExperienceLevel(
                    Application.ExperienceLevel.valueOf((String) requestData.get("experienceLevel")));
            application.setJobType(
                    Application.JobType.valueOf((String) requestData.get("jobType")));
            application.setCoverLetter((String) requestData.get("coverLetter"));

            // 선택적 필드들
            if (requestData.containsKey("skills")) {
                List<String> skills = (List<String>) requestData.get("skills");
                application.setSkills(String.join(",", skills));
            }
            application.setExpectedSalary((String) requestData.get("expectedSalary"));
            application.setAvailableStartDate((String) requestData.get("availableStartDate"));
            application.setWorkLocation((String) requestData.get("workLocation"));

            Application savedApplication = applicationRepository.save(application);

            response.put("success", true);
            response.put("message", "🎉 지원서가 성공적으로 제출되었습니다!");
            response.put("applicationId", savedApplication.getId());
            response.put("company", savedApplication.getCompany());
            response.put("position", savedApplication.getPosition());
            response.put("status", savedApplication.getStatus());

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "지원서 제출 중 오류: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * 내 지원서 목록 조회
     */
    @GetMapping("/my-list")
    public ResponseEntity<Map<String, Object>> getMyApplications(
            @AuthenticationPrincipal OAuth2User oauth2User) {

        Map<String, Object> response = new HashMap<>();

        if (oauth2User == null) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        try {
            Long kakaoId = Long.valueOf(oauth2User.getAttribute("id").toString());
            Optional<User> userOpt = userRepository.findByKakaoId(String.valueOf(kakaoId));

            if (!userOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "사용자 정보를 찾을 수 없습니다");
                return ResponseEntity.badRequest().body(response);
            }

            User user = userOpt.get();
            List<Application> applications = applicationRepository.findByUserOrderByCreatedAtDesc(user);

            response.put("success", true);
            response.put("totalCount", applications.size());
            response.put("applications", applications.stream().map(app -> {
                Map<String, Object> appMap = new HashMap<>();
                appMap.put("id", app.getId());
                appMap.put("company", app.getCompany());
                appMap.put("position", app.getPosition());
                appMap.put("status", app.getStatus().getDescription());
                appMap.put("submittedAt", app.getCreatedAt());
                return appMap;
            }).toList());

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "목록 조회 오류: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        return ResponseEntity.ok(response);
    }
}