package com.gamecraft.studios.repository;

import com.gamecraft.studios.entity.JobPosition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JobPositionRepository extends JpaRepository<JobPosition, Long> {

    // 인기 채용공고 (조회수 + 지원수 기준)
    @Query("SELECT j FROM JobPosition j WHERE j.status = :status ORDER BY j.viewCount DESC, j.applicationCount DESC")
    Page<JobPosition> findPopularJobs(@Param("status") JobPosition.Status status, Pageable pageable);

    // 최신 채용공고
    @Query("SELECT j FROM JobPosition j WHERE j.status = :status AND j.createdAt >= :since ORDER BY j.createdAt DESC")
    List<JobPosition> findRecentJobs(@Param("status") JobPosition.Status status, @Param("since") LocalDateTime since);

    // 회사별 채용공고 수
    @Query("SELECT j.company, COUNT(j) FROM JobPosition j WHERE j.status = :status GROUP BY j.company")
    List<Object[]> findJobCountByCompany(@Param("status") JobPosition.Status status);

    // 지역별 채용공고 수
    @Query("SELECT j.location, COUNT(j) FROM JobPosition j WHERE j.status = :status GROUP BY j.location")
    List<Object[]> findJobCountByLocation(@Param("status") JobPosition.Status status);

    // 경력별 채용공고 수
    @Query("SELECT j.experienceLevel, COUNT(j) FROM JobPosition j WHERE j.status = :status GROUP BY j.experienceLevel")
    List<Object[]> findJobCountByExperienceLevel(@Param("status") JobPosition.Status status);

    // 상태별 개수
    long countByStatus(JobPosition.Status status);

    // 회사명으로 검색
    List<JobPosition> findByCompanyContainingIgnoreCaseAndStatus(String company, JobPosition.Status status);

    // 직무 제목으로 검색
    List<JobPosition> findByTitleContainingIgnoreCaseAndStatus(String title, JobPosition.Status status);
}