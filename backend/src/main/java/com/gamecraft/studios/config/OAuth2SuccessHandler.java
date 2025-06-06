package com.gamecraft.studios.config;

import com.gamecraft.studios.entity.User;
import com.gamecraft.studios.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Optional;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        try {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

            // 카카오 사용자 정보 추출
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

            // 사용자 저장 또는 업데이트
            Optional<User> existingUser = userRepository.findByKakaoId(String.valueOf(kakaoId));
            User user;

            if (existingUser.isPresent()) {
                user = existingUser.get();
                // 기존 사용자 정보 업데이트
                user.setName(nickname);
                user.setEmail(email);
                user.setProfileImage(profileImage);
                logger.info("기존 사용자 로그인: " + user.getName());
            } else {
                // 새 사용자 생성
                user = new User();
                user.setKakaoId(String.valueOf(kakaoId));
                user.setName(nickname);
                user.setEmail(email);
                user.setProfileImage(profileImage);
                user.setRole(User.Role.USER);
                user.setStatus(User.Status.ACTIVE);
                logger.info("새 사용자 생성: " + user.getName());
            }

            userRepository.save(user);

            // 프론트엔드로 리다이렉트 (성공)
            String redirectUrl = "http://localhost:3000/auth/callback?success=true";
            getRedirectStrategy().sendRedirect(request, response, redirectUrl);

        } catch (Exception e) {
            logger.error("OAuth2 success handling failed", e);

            // 에러 발생 시 프론트엔드로 에러와 함께 리다이렉트
            String errorMessage = URLEncoder.encode("로그인 처리 중 오류가 발생했습니다.", "UTF-8");
            String redirectUrl = "http://localhost:3000/auth/callback?error=" + errorMessage;
            getRedirectStrategy().sendRedirect(request, response, redirectUrl);
        }
    }
}