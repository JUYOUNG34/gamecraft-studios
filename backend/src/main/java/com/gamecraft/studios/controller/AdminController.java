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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 관리자 권한 확인
     */
    private boolean isAdmin(OAuth2User oauth2User) {
        if (oauth2User == null) return false;

        Long kakaoId = Long.valueOf(oauth2User.getAttribute("id").toString());
        Optional<User> userOpt = userRepository.findByKakaoId(String.valueOf(kakaoId));

        return userOpt.isPresent() && userOpt.get().getRole() == User.Role.ADMIN;
    }

    /**
     * 현재 사용자를 관리자로 승격 (개발용)
     */
    @PostMapping("/promote-to-admin")
    public ResponseEntity<Map<String, Object>> promoteToAdmin(
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
            user.setRole(User.Role.ADMIN);
            userRepository.save(user);

            response.put("success", true);
            response.put("message", "🔑 관리자 권한이 부여되었습니다!");
            response.put("user", Map.of(
                    "id", user.getId(),
                    "name", user.getName(),
                    "role", user.getRole().name()
            ));

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "권한 부여 중 오류: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * 관리자 대시보드 메인 - 통계 정보
     */
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard(
            @AuthenticationPrincipal OAuth2User oauth2User) {

        Map<String, Object> response = new HashMap<>();

        if (!isAdmin(oauth2User)) {
            response.put("success", false);
            response.put("message", "관리자 권한이 필요합니다");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        try {
            // 전체 통계
            long totalApplications = applicationRepository.count();
            long totalUsers = userRepository.count();

            // 상태별 통계
            Map<String, Long> statusStats = new HashMap<>();
            for (Application.Status status : Application.Status.values()) {
                long count = applicationRepository.findByStatusOrderByCreatedAtDesc(status).size();
                statusStats.put(status.name(), count);
            }

            // 회사별 통계
            List<Object[]> companyStats = applicationRepository.findApplicationCountByCompany();
            Map<String, Object> companyData = new HashMap<>();
            for (Object[] stat : companyStats) {
                companyData.put((String) stat[0], stat[1]);
            }

            // 최근 지원서들
            List<Application> recentApps = applicationRepository.findRecentApplications(
                    LocalDateTime.now().minusDays(7)
            );

            response.put("success", true);
            response.put("statistics", Map.of(
                    "totalApplications", totalApplications,
                    "totalUsers", totalUsers,
                    "statusStats", statusStats,
                    "companyStats", companyData,
                    "recentApplicationsCount", recentApps.size()
            ));

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "대시보드 조회 오류: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * 모든 지원서 목록 조회 (페이징)
     */
    @GetMapping("/applications")
    public ResponseEntity<Map<String, Object>> getAllApplications(
            @AuthenticationPrincipal OAuth2User oauth2User,
            @RequestParam(defaultValue = "ALL") String status,
            @RequestParam(defaultValue = "") String company) {

        Map<String, Object> response = new HashMap<>();

        if (!isAdmin(oauth2User)) {
            response.put("success", false);
            response.put("message", "관리자 권한이 필요합니다");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        try {
            List<Application> applications;

            // 상태별 필터링
            if ("ALL".equals(status)) {
                applications = applicationRepository.findAll();
            } else {
                Application.Status statusEnum = Application.Status.valueOf(status);
                applications = applicationRepository.findByStatusOrderByCreatedAtDesc(statusEnum);
            }

            // 회사별 필터링
            if (!company.isEmpty()) {
                applications = applications.stream()
                        .filter(app -> app.getCompany().toLowerCase().contains(company.toLowerCase()))
                        .toList();
            }

            // 최신순 정렬
            applications = applications.stream()
                    .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                    .toList();

            response.put("success", true);
            response.put("totalCount", applications.size());
            response.put("applications", applications.stream().map(app -> {
                Map<String, Object> appData = new HashMap<>();
                appData.put("id", app.getId());
                appData.put("applicantName", app.getUser().getName());
                appData.put("applicantEmail", app.getUser().getEmail());
                appData.put("company", app.getCompany());
                appData.put("position", app.getPosition());
                appData.put("status", app.getStatus());
                appData.put("statusDescription", app.getStatus().getDescription());
                appData.put("experienceLevel", app.getExperienceLevel());
                appData.put("jobType", app.getJobType());
                appData.put("submittedAt", app.getCreatedAt());
                appData.put("updatedAt", app.getUpdatedAt());
                return appData;
            }).toList());

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "지원서 목록 조회 오류: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * 지원서 상세 조회 (관리자용)
     */
    @GetMapping("/applications/{applicationId}")
    public ResponseEntity<Map<String, Object>> getApplicationDetail(
            @AuthenticationPrincipal OAuth2User oauth2User,
            @PathVariable Long applicationId) {

        Map<String, Object> response = new HashMap<>();

        if (!isAdmin(oauth2User)) {
            response.put("success", false);
            response.put("message", "관리자 권한이 필요합니다");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        try {
            Optional<Application> appOpt = applicationRepository.findById(applicationId);

            if (!appOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "지원서를 찾을 수 없습니다");
                return ResponseEntity.notFound().build();
            }

            Application app = appOpt.get();
            User applicant = app.getUser();

            Map<String, Object> appDetail = new HashMap<>();
            appDetail.put("id", app.getId());
            appDetail.put("company", app.getCompany());
            appDetail.put("position", app.getPosition());
            appDetail.put("experienceLevel", app.getExperienceLevel());
            appDetail.put("jobType", app.getJobType());
            appDetail.put("skills", app.getSkills() != null ? app.getSkills().split(",") : new String[0]);
            appDetail.put("coverLetter", app.getCoverLetter());
            appDetail.put("expectedSalary", app.getExpectedSalary());
            appDetail.put("availableStartDate", app.getAvailableStartDate());
            appDetail.put("workLocation", app.getWorkLocation());
            appDetail.put("status", app.getStatus());
            appDetail.put("statusDescription", app.getStatus().getDescription());
            appDetail.put("adminNotes", app.getAdminNotes());
            appDetail.put("submittedAt", app.getCreatedAt());
            appDetail.put("updatedAt", app.getUpdatedAt());

            // 지원자 정보
            appDetail.put("applicant", Map.of(
                    "id", applicant.getId(),
                    "name", applicant.getName(),
                    "email", applicant.getEmail(),
                    "phone", applicant.getPhone() != null ? applicant.getPhone() : "",
                    "github", applicant.getGithub() != null ? applicant.getGithub() : "",
                    "portfolio", applicant.getPortfolio() != null ? applicant.getPortfolio() : "",
                    "profileImage", applicant.getProfileImage()
            ));

            response.put("success", true);
            response.put("application", appDetail);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "지원서 조회 오류: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * 지원서 상태 변경
     */
    @PutMapping("/applications/{applicationId}/status")
    public ResponseEntity<Map<String, Object>> updateApplicationStatus(
            @AuthenticationPrincipal OAuth2User oauth2User,
            @PathVariable Long applicationId,
            @RequestBody Map<String, Object> requestData) {

        Map<String, Object> response = new HashMap<>();

        if (!isAdmin(oauth2User)) {
            response.put("success", false);
            response.put("message", "관리자 권한이 필요합니다");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        try {
            Optional<Application> appOpt = applicationRepository.findById(applicationId);

            if (!appOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "지원서를 찾을 수 없습니다");
                return ResponseEntity.notFound().build();
            }

            Application app = appOpt.get();

            // 상태 변경
            String newStatus = (String) requestData.get("status");
            Application.Status status = Application.Status.valueOf(newStatus);
            app.setStatus(status);

            // 관리자 메모 추가
            if (requestData.containsKey("adminNotes")) {
                String notes = (String) requestData.get("adminNotes");
                app.setAdminNotes(notes);
            }

            Application updatedApp = applicationRepository.save(app);

            response.put("success", true);
            response.put("message", "✅ 지원서 상태가 변경되었습니다: " + status.getDescription());
            response.put("applicationId", updatedApp.getId());
            response.put("newStatus", updatedApp.getStatus());
            response.put("statusDescription", updatedApp.getStatus().getDescription());
            response.put("updatedAt", updatedApp.getUpdatedAt());

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "상태 변경 오류: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        return ResponseEntity.ok(response);
    }
}