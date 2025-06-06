package com.gamecraft.studios.repository;

import com.gamecraft.studios.entity.Application;
import com.gamecraft.studios.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    /**
     * 특정 사용자의 모든 지원서 조회
     */
    List<Application> findByUserOrderByCreatedAtDesc(User user);

    /**
     * 사용자 ID로 지원서 조회
     */
    List<Application> findByUserIdOrderByCreatedAtDesc(Long userId);

    /**
     * 특정 상태의 지원서들 조회
     */
    List<Application> findByStatusOrderByCreatedAtDesc(Application.Status status);

    /**
     * 특정 회사의 지원서들 조회
     */
    List<Application> findByCompanyIgnoreCaseOrderByCreatedAtDesc(String company);

    /**
     * 특정 포지션의 지원서들 조회
     */
    List<Application> findByPositionContainingIgnoreCaseOrderByCreatedAtDesc(String position);

    /**
     * 경력 레벨별 지원서 조회
     */
    List<Application> findByExperienceLevelOrderByCreatedAtDesc(Application.ExperienceLevel experienceLevel);

    /**
     * 근무 형태별 지원서 조회
     */
    List<Application> findByJobTypeOrderByCreatedAtDesc(Application.JobType jobType);

    /**
     * 특정 기간 내 지원서 조회
     */
    @Query("SELECT a FROM Application a WHERE a.createdAt BETWEEN :startDate AND :endDate ORDER BY a.createdAt DESC")
    List<Application> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate,
                                             @Param("endDate") LocalDateTime endDate);

    /**
     * 사용자가 특정 회사에 이미 지원했는지 확인
     */
    @Query("SELECT a FROM Application a WHERE a.user.id = :userId AND LOWER(a.company) = LOWER(:company)")
    Optional<Application> findByUserIdAndCompanyIgnoreCase(@Param("userId") Long userId,
                                                           @Param("company") String company);

    /**
     * 면접 관련 상태의 지원서들 조회
     */
    @Query("SELECT a FROM Application a WHERE a.status IN ('INTERVIEW_SCHEDULED', 'INTERVIEW_COMPLETED') ORDER BY a.updatedAt DESC")
    List<Application> findInterviewApplications();

    /**
     * 최종 결과가 나온 지원서들 조회
     */
    @Query("SELECT a FROM Application a WHERE a.status IN ('ACCEPTED', 'REJECTED') ORDER BY a.updatedAt DESC")
    List<Application> findCompletedApplications();

    /**
     * 검토가 필요한 지원서들 조회 (제출됨, 검토중)
     */
    @Query("SELECT a FROM Application a WHERE a.status IN ('SUBMITTED', 'REVIEWING') ORDER BY a.createdAt ASC")
    List<Application> findPendingReviewApplications();

    /**
     * 특정 기술 스택을 포함한 지원서 검색
     */
    @Query("SELECT a FROM Application a WHERE a.skills LIKE %:skill% ORDER BY a.createdAt DESC")
    List<Application> findBySkillsContaining(@Param("skill") String skill);

    /**
     * 회사별 지원서 통계
     */
    @Query("SELECT a.company, COUNT(a) as count FROM Application a GROUP BY a.company ORDER BY count DESC")
    List<Object[]> findApplicationCountByCompany();

    /**
     * 상태별 지원서 통계
     */
    @Query("SELECT a.status, COUNT(a) as count FROM Application a GROUP BY a.status")
    List<Object[]> findApplicationCountByStatus();

    /**
     * 최근 N일간의 지원서 조회
     */
    @Query("SELECT a FROM Application a WHERE a.createdAt >= :date ORDER BY a.createdAt DESC")
    List<Application> findRecentApplications(@Param("date") LocalDateTime date);
}