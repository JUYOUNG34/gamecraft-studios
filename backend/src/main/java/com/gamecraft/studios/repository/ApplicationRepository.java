package com.gamecraft.studios.repository;

import com.gamecraft.studios.entity.Application;
import com.gamecraft.studios.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    // AdminController에서 필요한 메서드 (에러 해결)
    List<Application> findByStatusOrderByCreatedAtDesc(Application.Status status);

    // 기본 메서드들
    List<Application> findByUserOrderByCreatedAtDesc(User user);
    Page<Application> findByUser(User user, Pageable pageable);
    List<Application> findByCompanyOrderByCreatedAtDesc(String company);
    List<Application> findByStatus(Application.Status status);

    // 회사 + 상태별 지원서 조회
    List<Application> findByCompanyAndStatus(String company, Application.Status status);

    // 최근 지원서 조회
    @Query("SELECT a FROM Application a WHERE a.createdAt >= :since ORDER BY a.createdAt DESC")
    List<Application> findRecentApplications(@Param("since") LocalDateTime since);

    // 회사별 지원서 수 통계
    @Query("SELECT a.company, COUNT(a) FROM Application a GROUP BY a.company ORDER BY COUNT(a) DESC")
    List<Object[]> findApplicationCountByCompany();

    // 상태별 지원서 수 통계
    @Query("SELECT a.status, COUNT(a) FROM Application a GROUP BY a.status")
    List<Object[]> findApplicationCountByStatus();

    // 특정 기간 지원서 수
    @Query("SELECT COUNT(a) FROM Application a WHERE a.createdAt BETWEEN :start AND :end")
    long countApplicationsBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // 사용자의 특정 회사 지원 여부 확인
    boolean existsByUserAndCompany(User user, String company);

    // 전체 지원서 수 조회
    @Query("SELECT COUNT(a) FROM Application a")
    long countAllApplications();

    // ⭐ 오늘 접수된 지원서 수 (가장 간단한 방법)
    @Query("SELECT COUNT(a) FROM Application a WHERE a.createdAt >= CURRENT_DATE")
    long countTodayApplications();

    // 특정 사용자의 지원서 수
    @Query("SELECT COUNT(a) FROM Application a WHERE a.user = :user")
    long countByUser(@Param("user") User user);

    // 상태 및 회사별 필터링 (관리자 대시보드용)
    @Query("SELECT a FROM Application a WHERE " +
            "(:status IS NULL OR a.status = :status) AND " +
            "(:company IS NULL OR a.company LIKE %:company%) " +
            "ORDER BY a.createdAt DESC")
    Page<Application> findByStatusAndCompanyWithPaging(
            @Param("status") Application.Status status,
            @Param("company") String company,
            Pageable pageable
    );

    // 지원자 이름으로 검색 (관리자용)
    @Query("SELECT a FROM Application a WHERE a.user.name LIKE %:name% ORDER BY a.createdAt DESC")
    List<Application> findByApplicantNameContaining(@Param("name") String name);

    // 최근 N일간의 지원서 통계 (PostgreSQL 완전 호환)
    @Query("SELECT CAST(a.createdAt AS date) as date, COUNT(a) as count " +
            "FROM Application a " +
            "WHERE a.createdAt >= :since " +
            "GROUP BY CAST(a.createdAt AS date) " +
            "ORDER BY CAST(a.createdAt AS date) DESC")
    List<Object[]> getDailyApplicationStats(@Param("since") LocalDateTime since);
}