package com.gamecraft.studios.controller;

import com.gamecraft.studios.entity.Application;
import com.gamecraft.studios.entity.User;
import com.gamecraft.studios.repository.ApplicationRepository;
import com.gamecraft.studios.repository.UserRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/analytics")
public class AnalyticsController {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private UserRepository userRepository;

    // 관리자 권한 확인
    private boolean isAdmin(OAuth2User oauth2User) {
        if (oauth2User == null) return false;
        Long kakaoId = Long.valueOf(oauth2User.getAttribute("id").toString());
        Optional<User> userOpt = userRepository.findByKakaoId(String.valueOf(kakaoId));
        return userOpt.isPresent() && userOpt.get().getRole() == User.Role.ADMIN;
    }

    /**
     * 📊 종합 분석 대시보드
     */
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getAnalyticsDashboard(
            @AuthenticationPrincipal OAuth2User oauth2User,
            @RequestParam(defaultValue = "30") int days) {

        Map<String, Object> response = new HashMap<>();

        if (!isAdmin(oauth2User)) {
            response.put("success", false);
            response.put("message", "관리자 권한이 필요합니다");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        try {
            LocalDateTime startDate = LocalDateTime.now().minusDays(days);
            List<Application> applications = applicationRepository.findAll();
            List<Application> recentApps = applications.stream()
                    .filter(app -> app.getCreatedAt().isAfter(startDate))
                    .collect(Collectors.toList());

            // 1. 기본 통계
            Map<String, Object> basicStats = new HashMap<>();
            basicStats.put("totalApplications", applications.size());
            basicStats.put("recentApplications", recentApps.size());
            basicStats.put("totalUsers", userRepository.count());
            basicStats.put("analysisPeriod", days + "일");

            // 2. 시간별 트렌드 (일별)
            Map<String, Long> dailyTrend = getDailyTrend(recentApps, days);

            // 3. 주별 트렌드
            Map<String, Long> weeklyTrend = getWeeklyTrend(applications);

            // 4. 월별 트렌드
            Map<String, Long> monthlyTrend = getMonthlyTrend(applications);

            // 5. 경력별 분석
            Map<String, Object> experienceAnalysis = getExperienceAnalysis(applications);

            // 6. 회사별 분석
            Map<String, Object> companyAnalysis = getCompanyAnalysis(applications);

            // 7. 상태별 분석
            Map<String, Object> statusAnalysis = getStatusAnalysis(applications);

            // 8. 직무별 분석
            Map<String, Object> positionAnalysis = getPositionAnalysis(applications);

            response.put("success", true);
            response.put("basicStats", basicStats);
            response.put("trends", Map.of(
                    "daily", dailyTrend,
                    "weekly", weeklyTrend,
                    "monthly", monthlyTrend
            ));
            response.put("experienceAnalysis", experienceAnalysis);
            response.put("companyAnalysis", companyAnalysis);
            response.put("statusAnalysis", statusAnalysis);
            response.put("positionAnalysis", positionAnalysis);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "분석 데이터 조회 오류: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * 📈 일별 트렌드 분석
     */
    private Map<String, Long> getDailyTrend(List<Application> applications, int days) {
        Map<String, Long> dailyTrend = new LinkedHashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");

        for (int i = days - 1; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            String dateStr = date.format(formatter);

            long count = applications.stream()
                    .filter(app -> app.getCreatedAt().toLocalDate().equals(date))
                    .count();

            dailyTrend.put(dateStr, count);
        }

        return dailyTrend;
    }

    /**
     * 📅 주별 트렌드 분석
     */
    private Map<String, Long> getWeeklyTrend(List<Application> applications) {
        Map<String, Long> weeklyTrend = new LinkedHashMap<>();

        for (int i = 11; i >= 0; i--) {
            LocalDate weekStart = LocalDate.now().minusWeeks(i);
            LocalDate weekEnd = weekStart.plusDays(6);

            String weekStr = weekStart.format(DateTimeFormatter.ofPattern("MM-dd")) +
                    " ~ " + weekEnd.format(DateTimeFormatter.ofPattern("MM-dd"));

            long count = applications.stream()
                    .filter(app -> {
                        LocalDate appDate = app.getCreatedAt().toLocalDate();
                        return !appDate.isBefore(weekStart) && !appDate.isAfter(weekEnd);
                    })
                    .count();

            weeklyTrend.put(weekStr, count);
        }

        return weeklyTrend;
    }

    /**
     * 📆 월별 트렌드 분석
     */
    private Map<String, Long> getMonthlyTrend(List<Application> applications) {
        Map<String, Long> monthlyTrend = new LinkedHashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        for (int i = 11; i >= 0; i--) {
            LocalDate month = LocalDate.now().minusMonths(i);
            String monthStr = month.format(formatter);

            long count = applications.stream()
                    .filter(app -> app.getCreatedAt().format(formatter).equals(monthStr))
                    .count();

            monthlyTrend.put(monthStr, count);
        }

        return monthlyTrend;
    }

    /**
     * 👨‍💼 경력별 분석
     */
    private Map<String, Object> getExperienceAnalysis(List<Application> applications) {
        Map<String, Object> analysis = new HashMap<>();

        // 경력별 지원자 수
        Map<String, Long> experienceCount = applications.stream()
                .collect(Collectors.groupingBy(
                        app -> app.getExperienceLevel().name(),
                        Collectors.counting()
                ));

        // 경력별 합격률
        Map<String, Double> acceptanceRate = new HashMap<>();
        for (Application.ExperienceLevel level : Application.ExperienceLevel.values()) {
            long total = applications.stream()
                    .filter(app -> app.getExperienceLevel() == level)
                    .count();

            long accepted = applications.stream()
                    .filter(app -> app.getExperienceLevel() == level &&
                            app.getStatus() == Application.Status.ACCEPTED)
                    .count();

            double rate = total > 0 ? (double) accepted / total * 100 : 0;
            acceptanceRate.put(level.name(), Math.round(rate * 100.0) / 100.0);
        }

        analysis.put("count", experienceCount);
        analysis.put("acceptanceRate", acceptanceRate);
        return analysis;
    }

    /**
     * 🏢 회사별 분석
     */
    private Map<String, Object> getCompanyAnalysis(List<Application> applications) {
        Map<String, Object> analysis = new HashMap<>();

        // 회사별 지원자 수
        Map<String, Long> companyCount = applications.stream()
                .collect(Collectors.groupingBy(
                        Application::getCompany,
                        Collectors.counting()
                ));

        // 회사별 평균 처리 시간 (일)
        Map<String, Double> avgProcessingTime = new HashMap<>();
        Map<String, List<Application>> companyCounts = applications.stream()
                .filter(app -> app.getStatus() != Application.Status.SUBMITTED)
                .collect(Collectors.groupingBy(Application::getCompany));

        for (Map.Entry<String, List<Application>> entry : companyCounts.entrySet()) {
            double avgDays = entry.getValue().stream()
                    .mapToDouble(app -> ChronoUnit.DAYS.between(
                            app.getCreatedAt(), app.getUpdatedAt()))
                    .average()
                    .orElse(0.0);
            avgProcessingTime.put(entry.getKey(), Math.round(avgDays * 10.0) / 10.0);
        }

        analysis.put("count", companyCount);
        analysis.put("avgProcessingTime", avgProcessingTime);
        return analysis;
    }

    /**
     * 📋 상태별 분석
     */
    private Map<String, Object> getStatusAnalysis(List<Application> applications) {
        Map<String, Object> analysis = new HashMap<>();

        Map<String, Long> statusCount = applications.stream()
                .collect(Collectors.groupingBy(
                        app -> app.getStatus().name(),
                        Collectors.counting()
                ));

        // 전체 대비 비율
        int total = applications.size();
        Map<String, Double> statusPercentage = new HashMap<>();
        for (Map.Entry<String, Long> entry : statusCount.entrySet()) {
            double percentage = total > 0 ? (double) entry.getValue() / total * 100 : 0;
            statusPercentage.put(entry.getKey(), Math.round(percentage * 100.0) / 100.0);
        }

        analysis.put("count", statusCount);
        analysis.put("percentage", statusPercentage);
        return analysis;
    }

    /**
     * 💼 직무별 분석
     */
    private Map<String, Object> getPositionAnalysis(List<Application> applications) {
        Map<String, Object> analysis = new HashMap<>();

        Map<String, Long> positionCount = applications.stream()
                .collect(Collectors.groupingBy(
                        Application::getPosition,
                        Collectors.counting()
                ));

        // 직무별 경쟁률 (지원자 수)
        Map<String, Object> competition = new HashMap<>();
        for (Map.Entry<String, Long> entry : positionCount.entrySet()) {
            competition.put(entry.getKey(), entry.getValue() + ":1");
        }

        analysis.put("count", positionCount);
        analysis.put("competition", competition);
        return analysis;
    }

    /**
     * 📊 Excel 다운로드
     */
    @GetMapping("/download/excel")
    public ResponseEntity<byte[]> downloadExcel(@AuthenticationPrincipal OAuth2User oauth2User) {

        if (!isAdmin(oauth2User)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            List<Application> applications = applicationRepository.findAll();

            Workbook workbook = new XSSFWorkbook();

            // 시트 1: 지원서 목록
            createApplicationSheet(workbook, applications);

            // 시트 2: 통계 요약
            createStatisticsSheet(workbook, applications);

            // 시트 3: 회사별 분석
            createCompanyAnalysisSheet(workbook, applications);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment",
                    "gamecraft_applications_" + LocalDate.now() + ".xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(outputStream.toByteArray());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Excel 시트 생성 - 지원서 목록
     */
    private void createApplicationSheet(Workbook workbook, List<Application> applications) {
        Sheet sheet = workbook.createSheet("지원서 목록");

        // 헤더 스타일
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        // 헤더 생성
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "지원자명", "이메일", "회사", "포지션", "경력", "고용형태",
                "상태", "지원일", "수정일", "급여", "근무지", "시작가능일"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // 데이터 행 생성
        int rowIndex = 1;
        for (Application app : applications) {
            Row row = sheet.createRow(rowIndex++);

            row.createCell(0).setCellValue(app.getId());
            row.createCell(1).setCellValue(app.getUser().getName());
            row.createCell(2).setCellValue(app.getUser().getEmail());
            row.createCell(3).setCellValue(app.getCompany());
            row.createCell(4).setCellValue(app.getPosition());
            row.createCell(5).setCellValue(app.getExperienceLevel().name());
            row.createCell(6).setCellValue(app.getJobType().name());
            row.createCell(7).setCellValue(app.getStatus().getDescription());
            row.createCell(8).setCellValue(app.getCreatedAt().toString());
            row.createCell(9).setCellValue(app.getUpdatedAt().toString());
            row.createCell(10).setCellValue(app.getExpectedSalary() != null ? app.getExpectedSalary() : "");
            row.createCell(11).setCellValue(app.getWorkLocation() != null ? app.getWorkLocation() : "");
            row.createCell(12).setCellValue(app.getAvailableStartDate() != null ? app.getAvailableStartDate() : "");
        }

        // 열 너비 자동 조정
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    /**
     * Excel 시트 생성 - 통계 요약
     */
    private void createStatisticsSheet(Workbook workbook, List<Application> applications) {
        Sheet sheet = workbook.createSheet("통계 요약");

        int rowIndex = 0;

        // 기본 통계
        sheet.createRow(rowIndex++).createCell(0).setCellValue("📊 기본 통계");
        sheet.createRow(rowIndex++).createCell(0).setCellValue("총 지원서 수: " + applications.size());
        sheet.createRow(rowIndex++).createCell(0).setCellValue("총 지원자 수: " + userRepository.count());
        rowIndex++;

        // 상태별 통계
        sheet.createRow(rowIndex++).createCell(0).setCellValue("📋 상태별 통계");
        Map<Application.Status, Long> statusStats = applications.stream()
                .collect(Collectors.groupingBy(Application::getStatus, Collectors.counting()));

        for (Map.Entry<Application.Status, Long> entry : statusStats.entrySet()) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(entry.getKey().getDescription());
            row.createCell(1).setCellValue(entry.getValue());
        }
        rowIndex++;

        // 회사별 통계
        sheet.createRow(rowIndex++).createCell(0).setCellValue("🏢 회사별 통계");
        Map<String, Long> companyStats = applications.stream()
                .collect(Collectors.groupingBy(Application::getCompany, Collectors.counting()));

        for (Map.Entry<String, Long> entry : companyStats.entrySet()) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(entry.getKey());
            row.createCell(1).setCellValue(entry.getValue());
        }
    }

    /**
     * Excel 시트 생성 - 회사별 분석
     */
    private void createCompanyAnalysisSheet(Workbook workbook, List<Application> applications) {
        Sheet sheet = workbook.createSheet("회사별 분석");

        // 헤더
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("회사명");
        headerRow.createCell(1).setCellValue("지원자 수");
        headerRow.createCell(2).setCellValue("합격자 수");
        headerRow.createCell(3).setCellValue("합격률 (%)");
        headerRow.createCell(4).setCellValue("평균 처리시간 (일)");

        Map<String, List<Application>> companyGroups = applications.stream()
                .collect(Collectors.groupingBy(Application::getCompany));

        int rowIndex = 1;
        for (Map.Entry<String, List<Application>> entry : companyGroups.entrySet()) {
            Row row = sheet.createRow(rowIndex++);

            String company = entry.getKey();
            List<Application> companyApps = entry.getValue();
            long total = companyApps.size();
            long accepted = companyApps.stream()
                    .filter(app -> app.getStatus() == Application.Status.ACCEPTED)
                    .count();
            double acceptanceRate = total > 0 ? (double) accepted / total * 100 : 0;

            double avgProcessingTime = companyApps.stream()
                    .filter(app -> app.getStatus() != Application.Status.SUBMITTED)
                    .mapToDouble(app -> ChronoUnit.DAYS.between(app.getCreatedAt(), app.getUpdatedAt()))
                    .average()
                    .orElse(0.0);

            row.createCell(0).setCellValue(company);
            row.createCell(1).setCellValue(total);
            row.createCell(2).setCellValue(accepted);
            row.createCell(3).setCellValue(Math.round(acceptanceRate * 100.0) / 100.0);
            row.createCell(4).setCellValue(Math.round(avgProcessingTime * 10.0) / 10.0);
        }

        // 열 너비 자동 조정
        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }

    }
}