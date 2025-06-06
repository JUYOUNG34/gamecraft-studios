package com.gamecraft.studios.controller;

import com.gamecraft.studios.entity.User;
import com.gamecraft.studios.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth/kakao")
public class KakaoAuthController {

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/login-url")
    public Map<String, String> getKakaoLoginUrl() {
        Map<String, String> response = new HashMap<>();
        response.put("loginUrl", "/oauth2/authorization/kakao");
        response.put("message", "카카오 로그인 URL");
        return response;
    }

    @GetMapping("/user-info")
    public Map<String, Object> getUserInfo(@AuthenticationPrincipal OAuth2User oauth2User) {
        Map<String, Object> response = new HashMap<>();

        if (oauth2User == null) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다");
            return response;
        }

        try {
            Long kakaoId = Long.valueOf(oauth2User.getAttribute("id").toString());
            Map<String, Object> kakaoAccount = oauth2User.getAttribute("kakao_account");

            String email = null;
            String nickname = null;
            String profileImage = null;

            if (kakaoAccount != null) {
                email = (String) kakaoAccount.get("email");

                Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
                if (profile != null) {
                    nickname = (String) profile.get("nickname");
                    profileImage = (String) profile.get("profile_image_url");
                }
            }

            Optional<User> existingUser = userRepository.findByKakaoId(String.valueOf(kakaoId));
            User user;

            if (existingUser.isPresent()) {
                user = existingUser.get();
                user.setName(nickname);
                user.setEmail(email);
                user.setProfileImage(profileImage);
                userRepository.save(user);

                response.put("userStatus", "기존 사용자 정보 업데이트됨");
            } else {
                user = new User();
                user.setKakaoId(String.valueOf(kakaoId));
                user.setName(nickname);
                user.setEmail(email);
                user.setProfileImage(profileImage);
                user.setRole(User.Role.USER);
                user.setStatus(User.Status.ACTIVE);
                userRepository.save(user);

                response.put("userStatus", "새 사용자 생성됨 - 카카오게임즈 지원 가능!");
            }

            response.put("success", true);
            response.put("message", "🎮 GameCraft Studios 사용자 등록 완료!");
            response.put("user", Map.of(
                    "id", user.getId(),
                    "kakaoId", kakaoId,
                    "name", nickname,
                    "email", email,
                    "profileImage", profileImage,
                    "role", user.getRole(),
                    "status", user.getStatus(),
                    "createdAt", user.getCreatedAt()
            ));
            response.put("nextSteps", Map.of(
                    "applicationForm", "/application/create",
                    "myApplications", "/application/my-list",
                    "profile", "/user/profile"
            ));

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "사용자 정보 처리 중 오류가 발생했습니다: " + e.getMessage());
        }

        return response;
    }


    @PostMapping("/logout")
    public Map<String, Object> logout() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "로그아웃 되었습니다");
        response.put("redirectUrl", "/");
        return response;
    }
}