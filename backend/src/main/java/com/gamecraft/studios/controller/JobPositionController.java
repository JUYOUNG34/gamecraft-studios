package com.gamecraft.studios.controller;

import com.gamecraft.studios.entity.JobPosition;
import com.gamecraft.studios.repository.JobPositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/positions")
public class JobPositionController {

    @Autowired
    private JobPositionRepository jobPositionRepository;

    /**
     * 채용공고 목록 조회 (필터링, 검색, 페이징)
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getJobPositions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String company,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String experienceLevel,
            @RequestParam(required = false) String jobType,
            @RequestParam(required = false) String skill,
            @RequestParam(defaultValue = "latest") String sort) {

        Map<String, Object> response = new HashMap<>();

        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<JobPosition> jobPage;

            // 정렬 방식 결정
            if ("popular".equals(sort)) {
                jobPage = jobPositionRepository.findPopularJobs(JobPosition.Status.ACTIVE, pageable);
            }
            // 검색어가 있는 경우
            else if (search != null && !search.trim().isEmpty()) {
                jobPage = jobPositionRepository.searchByKeyword(search.trim(), JobPosition.Status.ACTIVE, pageable);
            }
            // 기술 스택 검색
            else if (skill != null && !skill.trim().isEmpty()) {
                jobPage = jobPositionRepository.findBySkill(skill.trim(), JobPosition.Status.ACTIVE, pageable);
            }
            // 복합 필터링
            else {
                JobPosition.ExperienceLevel expLevel = null;
                if (experienceLevel != null && !experienceLevel.isEmpty()) {
                    try {
                        expLevel = JobPosition.ExperienceLevel.valueOf(experienceLevel);
                    } catch (IllegalArgumentException e) {
                        // 잘못된 경력 레벨 무시
                    }
                }

                JobPosition.JobType jType = null;
                if (jobType != null && !jobType.isEmpty()) {
                    try {
                        jType = JobPosition.JobType.valueOf(jobType);
                    } catch (IllegalArgumentException e) {
                        // 잘못된 고용 형태 무시
                    }
                }

                jobPage = jobPositionRepository.findByFilters(
                        company, location, expLevel, jType, JobPosition.Status.ACTIVE, pageable);
            }

            // 응답 데이터 구성
            List<Map<String, Object>> jobList = jobPage.getContent().stream().map(job -> {
                Map<String, Object> jobMap = new HashMap<>();
                jobMap.put("id", job.getId());
                jobMap.put("slug", job.getSlug());
                jobMap.put("title", job.getTitle());
                jobMap.put("company", job.getCompany());
                jobMap.put("companyLogo", job.getCompanyLogo());
                jobMap.put("location", job.getLocation());
                jobMap.put("jobType", job.getJobType());
                jobMap.put("jobTypeDescription", job.getJobType().getDescription());
                jobMap.put("experienceLevel", job.getExperienceLevel());
                jobMap.put("experienceLevelDescription", job.getExperienceLevel().getDescription());
                jobMap.put("salaryRange", job.getSalaryRange());
                jobMap.put("remoteWorkAvailable", job.getRemoteWorkAvailable());
                jobMap.put("viewCount", job.getViewCount());
                jobMap.put("applicationCount", job.getApplicationCount());
                jobMap.put("createdAt", job.getCreatedAt());
                jobMap.put("applicationDeadline", job.getApplicationDeadline());

                // 기술 스택 파싱
                if (job.getRequiredSkills() != null) {
                    jobMap.put("requiredSkills", job.getRequiredSkills().split(","));
                }
                if (job.getPreferredSkills() != null) {
                    jobMap.put("preferredSkills", job.getPreferredSkills().split(","));
                }

                return jobMap;
            }).toList();

            response.put("success", true);
            response.put("jobs", jobList);
            response.put("pagination", Map.of(
                    "currentPage", jobPage.getNumber(),
                    "totalPages", jobPage.getTotalPages(),
                    "totalElements", jobPage.getTotalElements(),
                    "size", jobPage.getSize(),
                    "hasNext", jobPage.hasNext(),
                    "hasPrevious", jobPage.hasPrevious()
            ));

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "채용공고 조회 오류: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * 채용공고 상세 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getJobPosition(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            Optional<JobPosition> jobOpt = jobPositionRepository.findById(id);

            if (!jobOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "채용공고를 찾을 수 없습니다");
                return ResponseEntity.notFound().build();
            }

            JobPosition job = jobOpt.get();

            // 조회수 증가
            job.incrementViewCount();
            jobPositionRepository.save(job);

            Map<String, Object> jobDetail = new HashMap<>();
            jobDetail.put("id", job.getId());
            jobDetail.put("slug", job.getSlug());
            jobDetail.put("title", job.getTitle());
            jobDetail.put("company", job.getCompany());
            jobDetail.put("companyLogo", job.getCompanyLogo());
            jobDetail.put("companyDescription", job.getCompanyDescription());
            jobDetail.put("description", job.getDescription());
            jobDetail.put("requirements", job.getRequirements());
            jobDetail.put("preferredQualifications", job.getPreferredQualifications());
            jobDetail.put("location", job.getLocation());
            jobDetail.put("jobType", job.getJobType());
            jobDetail.put("jobTypeDescription", job.getJobType().getDescription());
            jobDetail.put("experienceLevel", job.getExperienceLevel());
            jobDetail.put("experienceLevelDescription", job.getExperienceLevel().getDescription());
            jobDetail.put("salaryRange", job.getSalaryRange());
            jobDetail.put("remoteWorkAvailable", job.getRemoteWorkAvailable());
            jobDetail.put("benefits", job.getBenefits() != null ? job.getBenefits().split(",") : new String[0]);
            jobDetail.put("contactEmail", job.getContactEmail());
            jobDetail.put("contactPerson", job.getContactPerson());
            jobDetail.put("viewCount", job.getViewCount());
            jobDetail.put("applicationCount", job.getApplicationCount());
            jobDetail.put("createdAt", job.getCreatedAt());
            jobDetail.put("applicationDeadline", job.getApplicationDeadline());
            jobDetail.put("isActive", job.isActive());

            // 기술 스택
            if (job.getRequiredSkills() != null) {
                jobDetail.put("requiredSkills", job.getRequiredSkills().split(","));
            }
            if (job.getPreferredSkills() != null) {
                jobDetail.put("preferredSkills", job.getPreferredSkills().split(","));
            }

            response.put("success", true);
            response.put("job", jobDetail);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "채용공고 조회 오류: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * 슬러그로 채용공고 상세 조회
     */
    @GetMapping("/slug/{slug}")
    public ResponseEntity<Map<String, Object>> getJobPositionBySlug(@PathVariable String slug) {
        Map<String, Object> response = new HashMap<>();

        try {
            Optional<JobPosition> jobOpt = jobPositionRepository.findBySlugAndStatus(slug, JobPosition.Status.ACTIVE);

            if (!jobOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "채용공고를 찾을 수 없습니다");
                return ResponseEntity.notFound().build();
            }

            // ID로 상세 조회와 동일한 로직
            return getJobPosition(jobOpt.get().getId());

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "채용공고 조회 오류: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 인기 채용공고 조회
     */
    @GetMapping("/popular")
    public ResponseEntity<Map<String, Object>> getPopularJobs(
            @RequestParam(defaultValue = "6") int limit) {

        Map<String, Object> response = new HashMap<>();

        try {
            Pageable pageable = PageRequest.of(0, limit);
            Page<JobPosition> popularJobs = jobPositionRepository.findPopularJobs(JobPosition.Status.ACTIVE, pageable);

            List<Map<String, Object>> jobList = popularJobs.getContent().stream().map(job -> {
                Map<String, Object> jobMap = new HashMap<>();
                jobMap.put("id", job.getId());
                jobMap.put("slug", job.getSlug());
                jobMap.put("title", job.getTitle());
                jobMap.put("company", job.getCompany());
                jobMap.put("companyLogo", job.getCompanyLogo());
                jobMap.put("location", job.getLocation());
                jobMap.put("viewCount", job.getViewCount());
                jobMap.put("applicationCount", job.getApplicationCount());
                return jobMap;
            }).toList();

            response.put("success", true);
            response.put("jobs", jobList);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "인기 채용공고 조회 오류: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * 최신 채용공고 조회
     */
    @GetMapping("/recent")
    public ResponseEntity<Map<String, Object>> getRecentJobs(
            @RequestParam(defaultValue = "7") int days) {

        Map<String, Object> response = new HashMap<>();

        try {
            LocalDateTime since = LocalDateTime.now().minusDays(days);
            List<JobPosition> recentJobs = jobPositionRepository.findRecentJobs(JobPosition.Status.ACTIVE, since);

            List<Map<String, Object>> jobList = recentJobs.stream().map(job -> {
                Map<String, Object> jobMap = new HashMap<>();
                jobMap.put("id", job.getId());
                jobMap.put("slug", job.getSlug());
                jobMap.put("title", job.getTitle());
                jobMap.put("company", job.getCompany());
                jobMap.put("companyLogo", job.getCompanyLogo());
                jobMap.put("location", job.getLocation());
                jobMap.put("createdAt", job.getCreatedAt());
                return jobMap;
            }).toList();

            response.put("success", true);
            response.put("jobs", jobList);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "최신 채용공고 조회 오류: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * 마감임박 채용공고 조회
     */
    @GetMapping("/deadline-soon")
    public ResponseEntity<Map<String, Object>> getJobsWithDeadlineSoon(
            @RequestParam(defaultValue = "7") int days) {

        Map<String, Object> response = new HashMap<>();

        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime deadline = now.plusDays(days);

            List<JobPosition> urgentJobs = jobPositionRepository.findJobsWithDeadlineSoon(
                    JobPosition.Status.ACTIVE, now, deadline);

            List<Map<String, Object>> jobList = urgentJobs.stream().map(job -> {
                Map<String, Object> jobMap = new HashMap<>();
                jobMap.put("id", job.getId());
                jobMap.put("slug", job.getSlug());
                jobMap.put("title", job.getTitle());
                jobMap.put("company", job.getCompany());
                jobMap.put("companyLogo", job.getCompanyLogo());
                jobMap.put("location", job.getLocation());
                jobMap.put("applicationDeadline", job.getApplicationDeadline());
                return jobMap;
            }).toList();

            response.put("success", true);
            response.put("jobs", jobList);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "마감임박 채용공고 조회 오류: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * 채용공고 필터 옵션 조회
     */
    @GetMapping("/filter-options")
    public ResponseEntity<Map<String, Object>> getFilterOptions() {
        Map<String, Object> response = new HashMap<>();

        try {
            // 회사 목록
            List<Object[]> companyStats = jobPositionRepository.findJobCountByCompany(JobPosition.Status.ACTIVE);
            List<Map<String, Object>> companies = companyStats.stream().map(stat -> {
                Map<String, Object> company = new HashMap<>();
                company.put("name", stat[0]);
                company.put("count", stat[1]);
                return company;
            }).toList();

            // 지역 목록
            List<Object[]> locationStats = jobPositionRepository.findJobCountByLocation(JobPosition.Status.ACTIVE);
            List<Map<String, Object>> locations = locationStats.stream().map(stat -> {
                Map<String, Object> location = new HashMap<>();
                location.put("name", stat[0]);
                location.put("count", stat[1]);
                return location;
            }).toList();

            // 경력 레벨
            Map<String, String> experienceLevels = new HashMap<>();
            for (JobPosition.ExperienceLevel level : JobPosition.ExperienceLevel.values()) {
                experienceLevels.put(level.name(), level.getDescription());
            }

            // 고용 형태
            Map<String, String> jobTypes = new HashMap<>();
            for (JobPosition.JobType type : JobPosition.JobType.values()) {
                jobTypes.put(type.name(), type.getDescription());
            }

            response.put("success", true);
            response.put("companies", companies);
            response.put("locations", locations);
            response.put("experienceLevels", experienceLevels);
            response.put("jobTypes", jobTypes);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "필터 옵션 조회 오류: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * 채용공고 통계
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getJobStats() {
        Map<String, Object> response = new HashMap<>();

        try {
            long totalActiveJobs = jobPositionRepository.countByStatus(JobPosition.Status.ACTIVE);

            // 회사별 통계
            List<Object[]> companyStats = jobPositionRepository.findJobCountByCompany(JobPosition.Status.ACTIVE);

            // 지역별 통계
            List<Object[]> locationStats = jobPositionRepository.findJobCountByLocation(JobPosition.Status.ACTIVE);

            // 경력별 통계
            List<Object[]> experienceStats = jobPositionRepository.findJobCountByExperienceLevel(JobPosition.Status.ACTIVE);

            response.put("success", true);
            response.put("totalActiveJobs", totalActiveJobs);
            response.put("companyStats", companyStats);
            response.put("locationStats", locationStats);
            response.put("experienceStats", experienceStats);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "통계 조회 오류: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }
}