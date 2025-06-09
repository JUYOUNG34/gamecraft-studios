package com.gamecraft.studios.repository;

import com.gamecraft.studios.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByName(String name);

    List<Company> findByStatus(Company.Status status);

    Page<Company> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("SELECT c FROM Company c WHERE c.industry = :industry AND c.status = :status")
    List<Company> findByIndustryAndStatus(@Param("industry") String industry,
                                          @Param("status") Company.Status status);

    @Query("SELECT c FROM Company c LEFT JOIN FETCH c.jobPositions WHERE c.id = :id")
    Optional<Company> findByIdWithJobPositions(@Param("id") Long id);

    // 채용공고가 있는 회사들만 조회
    @Query("SELECT DISTINCT c FROM Company c JOIN c.jobPositions jp WHERE jp.status = 'ACTIVE'")
    List<Company> findCompaniesWithActiveJobs();
}
