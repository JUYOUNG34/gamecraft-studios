package com.gamecraft.studios.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public Map<String, Object> hello() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "🎮 GameCraft Studios Backend 정상 작동!");
        response.put("status", "OK");
        response.put("timestamp", System.currentTimeMillis());
        response.put("server", "Spring Boot 3.5.0");
        response.put("purpose", "카카오게임즈 지원 시스템");
        return response;
    }

    @GetMapping("/api-status")
    public Map<String, Object> status() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "GameCraft Studios Backend");
        response.put("status", "RUNNING");
        response.put("uptime", System.currentTimeMillis());
        response.put("database", "H2 In-Memory");
        response.put("profile", "dev");
        return response;
    }

    @GetMapping("/oauth-test")
    public Map<String, Object> oauthTest(@AuthenticationPrincipal OAuth2User oauth2User) {
        Map<String, Object> response = new HashMap<>();

        if (oauth2User != null) {
            response.put("success", true);
            response.put("message", "🎉 카카오 OAuth 성공!");
            response.put("kakaoId", oauth2User.getAttribute("id"));
            response.put("attributes", oauth2User.getAttributes());
        } else {
            response.put("success", false);
            response.put("message", "OAuth 실패 - 로그인이 필요합니다");
            response.put("loginUrl", "/oauth2/authorization/kakao");
        }

        return response;
    }

    @GetMapping("/api-guide")
    public Map<String, Object> apiGuide() {
        Map<String, Object> response = new HashMap<>();

        response.put("title", "🎮 GameCraft Studios API 가이드");
        response.put("description", "카카오게임즈 지원 시스템 API");

        Map<String, Object> auth = new HashMap<>();
        auth.put("login", "GET /oauth2/authorization/kakao - 카카오 로그인");
        auth.put("userInfo", "GET /auth/kakao/user-info - 사용자 정보 조회");
        auth.put("logout", "POST /auth/kakao/logout - 로그아웃");

        Map<String, Object> application = new HashMap<>();
        application.put("formInfo", "GET /application/form-info - 지원서 폼 정보");
        application.put("create", "POST /application/create - 지원서 작성");
        application.put("myList", "GET /application/my-list - 내 지원서 목록");
        application.put("detail", "GET /application/{id} - 지원서 상세 조회");
        application.put("update", "PUT /application/{id} - 지원서 수정");
        application.put("delete", "DELETE /application/{id} - 지원서 철회");

        response.put("auth", auth);
        response.put("application", application);
        response.put("note", "모든 지원서 API는 카카오 로그인이 필요합니다");

        return response;
    }
}