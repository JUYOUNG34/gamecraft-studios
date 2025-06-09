package com.gamecraft.studios.repository;

import com.gamecraft.studios.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 카카오 ID로 사용자 찾기
    Optional<User> findByKakaoId(String kakaoId);

    // 이메일로 사용자 찾기
    Optional<User> findByEmail(String email);

    // 이메일 중복 확인
    boolean existsByEmail(String email);

    // 카카오 ID 중복 확인
    boolean existsByKakaoId(String kakaoId);

    // 활성 사용자 수 조회
    @Query("SELECT COUNT(u) FROM User u WHERE u.status = 'ACTIVE'")
    long countActiveUsers();

    // 이름으로 사용자 검색 (관리자용)
    @Query("SELECT u FROM User u WHERE u.name LIKE %:name% ORDER BY u.createdAt DESC")
    List<User> findByNameContaining(@Param("name") String name);
}