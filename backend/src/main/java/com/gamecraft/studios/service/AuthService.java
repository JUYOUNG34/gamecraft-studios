package com.gamecraft.studios.service;

import com.gamecraft.studios.entity.User;
import com.gamecraft.studios.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final SecretKey secretKey;
    private final long jwtExpiration;

    public AuthService(UserRepository userRepository,
                       @Value("${jwt.secret}") String jwtSecret,
                       @Value("${jwt.expiration}") long jwtExpiration) {
        this.userRepository = userRepository;
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.jwtExpiration = jwtExpiration;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password("") // OAuth2 사용하므로 패스워드 불필요
                .authorities("ROLE_" + user.getRole().name())
                .build();
    }

    // JWT 토큰 생성 (최신 JJWT API)
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole().name());

        return Jwts.builder()
                .claims(claims)
                .subject(user.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(secretKey)
                .compact();
    }

    // JWT 토큰에서 사용자 이메일 추출
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // JWT 토큰에서 사용자 ID 추출
    public Long extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", Long.class));
    }

    // JWT 토큰 유효성 검사
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // 카카오 사용자 정보로 User 생성 또는 업데이트
    public User processKakaoUser(String kakaoId, String nickname, String email, String profileImage) {
        Optional<User> existingUser = userRepository.findByKakaoId(kakaoId);

        if (existingUser.isPresent()) {
            // 기존 사용자 정보 업데이트
            User user = existingUser.get();
            user.setName(nickname);
            user.setEmail(email);
            user.setProfileImage(profileImage);
            return userRepository.save(user);
        } else {
            // 새 사용자 생성
            User newUser = new User();
            newUser.setKakaoId(kakaoId);
            newUser.setName(nickname);
            newUser.setEmail(email != null ? email : kakaoId + "@kakao.temp");
            newUser.setProfileImage(profileImage);
            newUser.setRole(User.Role.USER);
            newUser.setStatus(User.Status.ACTIVE);
            return userRepository.save(newUser);
        }
    }
}