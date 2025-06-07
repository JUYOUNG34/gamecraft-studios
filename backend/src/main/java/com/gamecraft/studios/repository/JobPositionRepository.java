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
import java.util.Optional;

@Repository
public interface JobPositionRepository extends JpaRepository<JobPosition, Long> {


    Page<JobPosition> findByStatusOrderByCreatedAtDesc(JobPosition.Status status, Pageable pageable);


    Optional<JobPosition> findBySlugAndStatus(String slug, JobPosition.Status status);


    Page<JobPosition> findByCompanyIgnoreCaseAndStatusOrderByCreatedAtDesc(
            String company, JobPosition.Status status, Pageable pageable);


    Page<JobPosition> findByExperienceLevelAndStatusOrderByCreatedAtDesc(
            JobPosition.ExperienceLevel experienceLevel, JobPosition.Status status, Pageable pageable);


    Page<JobPosition> findByJobTypeAndStatusOrderByCreatedAtDesc(
            JobPosition.JobType jobType, JobPosition.Status status, Pageable pageable);


    Page<JobPosition> findByLocationContainingIgnoreCaseAndStatusOrderByCreatedAtDesc(
            String location, JobPosition.Status status, Pageable pageable);


    @Query("SELECT j FROM JobPosition j WHERE " +
            "(LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(j.company) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "j.status = :status " +
            "ORDER BY j.createdAt DESC")
    Page<JobPosition> searchByKeyword(@Param("keyword") String keyword,
                                      @Param("status") JobPosition.Status status,
                                      Pageable pageable);


    @Query("SELECT j FROM JobPosition j WHERE " +
            "(:company IS NULL OR LOWER(j.company) LIKE LOWER(CONCAT('%', :company, '%'))) AND " +
            "(:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
            "(:experienceLevel IS NULL OR j.experienceLevel = :experienceLevel) AND " +
            "(:jobType IS NULL OR j.jobType = :jobType) AND " +
            "j.status = :status " +
            "ORDER BY j.createdAt DESC")
    Page<JobPosition> findByFilters(@Param("company") String company,
                                    @Param("location") String location,
                                    @Param("experienceLevel") JobPosition.ExperienceLevel experienceLevel,
                                    @Param("jobType") JobPosition.JobType jobType,
                                    @Param("status") JobPosition.Status status,
                                    Pageable pageable);


    @Query("SELECT j FROM JobPosition j WHERE " +
            "(j.requiredSkills LIKE %:skill% OR j.preferredSkills LIKE %:skill%) AND " +
            "j.status = :status " +
            "ORDER BY j.createdAt DESC")
    Page<JobPosition> findBySkill(@Param("skill") String skill,
                                  @Param("status") JobPosition.Status status,
                                  Pageable pageable);


    @Query("SELECT j FROM JobPosition j WHERE j.status = :status AND j.createdAt >= :since ORDER BY j.createdAt DESC")
    List<JobPosition> findRecentJobs(@Param("status") JobPosition.Status status,
                                     @Param("since") LocalDateTime since);


    @Query("SELECT j FROM JobPosition j WHERE j.status = :status ORDER BY j.viewCount DESC, j.createdAt DESC")
    Page<JobPosition> findPopularJobs(@Param("status") JobPosition.Status status, Pageable pageable);


    @Query("SELECT j FROM JobPosition j WHERE " +
            "j.status = :status AND " +
            "j.applicationDeadline IS NOT NULL AND " +
            "j.applicationDeadline BETWEEN :now AND :deadline " +
            "ORDER BY j.applicationDeadline ASC")
    List<JobPosition> findJobsWithDeadlineSoon(@Param("status") JobPosition.Status status,
                                               @Param("now") LocalDateTime now,
                                               @Param("deadline") LocalDateTime deadline);


    @Query("SELECT j.company, COUNT(j) as count FROM JobPosition j WHERE j.status = :status GROUP BY j.company ORDER BY count DESC")
    List<Object[]> findJobCountByCompany(@Param("status") JobPosition.Status status);

    @Query("SELECT j.location, COUNT(j) as count FROM JobPosition j WHERE j.status = :status GROUP BY j.location ORDER BY count DESC")
    List<Object[]> findJobCountByLocation(@Param("status") JobPosition.Status status);

    @Query("SELECT j.experienceLevel, COUNT(j) as count FROM JobPosition j WHERE j.status = :status GROUP BY j.experienceLevel")
    List<Object[]> findJobCountByExperienceLevel(@Param("status") JobPosition.Status status);

    Page<JobPosition> findByRemoteWorkAvailableAndStatusOrderByCreatedAtDesc(
            Boolean remoteWorkAvailable, JobPosition.Status status, Pageable pageable);


    long countByStatus(JobPosition.Status status);

    long countByCompanyAndStatus(String company, JobPosition.Status status);
}