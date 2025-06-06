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


    Optional<User> findByKakaoId(String kakaoId);


    Optional<User> findByEmail(String email);


    boolean existsByEmail(String email);


    boolean existsByKakaoId(String kakaoId);


    List<User> findByStatus(User.Status status);


    List<User> findByRole(User.Role role);


    @Query("SELECT u FROM User u WHERE u.name LIKE %:name% AND u.status = 'ACTIVE'")
    List<User> findByNameContainingIgnoreCaseAndStatusActive(@Param("name") String name);


    @Query("SELECT u FROM User u WHERE u.status = 'ACTIVE' ORDER BY u.createdAt DESC")
    List<User> findRecentActiveUsers();


    @Query("SELECT u FROM User u WHERE u.github IS NOT NULL AND u.github != '' AND u.status = 'ACTIVE'")
    List<User> findUsersWithGithub();


    @Query("SELECT u FROM User u WHERE u.portfolio IS NOT NULL AND u.portfolio != '' AND u.status = 'ACTIVE'")
    List<User> findUsersWithPortfolio();
}