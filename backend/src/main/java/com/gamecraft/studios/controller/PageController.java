package com.gamecraft.studios.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class PageController {

    @GetMapping("/")
    public String home() {
        return "redirect:/hello-page";
    }

    @GetMapping("/hello-page")
    @ResponseBody
    public String helloPage() {
        return "<h1>🎮 GameCraft Studios</h1><p>카카오게임즈 지원 시스템</p><br><a href='/oauth2/authorization/kakao'>카카오 로그인</a>";
    }

    @GetMapping("/login")
    @ResponseBody
    public String login() {
        return "<h1>로그인</h1><br><a href='/oauth2/authorization/kakao'>카카오로 로그인</a>";
    }

    @GetMapping("/success")
    @ResponseBody
    public Map<String, Object> loginSuccess(@AuthenticationPrincipal OAuth2User oauth2User) {
        Map<String, Object> response = new HashMap<>();

        if (oauth2User != null) {
            response.put("success", true);
            response.put("message", "🎉 카카오 로그인 성공!");
            response.put("user", oauth2User.getAttributes());
            response.put("nextStep", "이제 /auth/kakao/user-info 를 테스트해보세요!");
        } else {
            response.put("success", false);
            response.put("message", "로그인 정보가 없습니다");
        }

        return response;
    }

    @GetMapping("/dashboard")
    @ResponseBody
    public Map<String, Object> dashboard(@AuthenticationPrincipal OAuth2User oauth2User) {
        Map<String, Object> response = new HashMap<>();
        response.put("page", "dashboard");
        response.put("user", oauth2User != null ? oauth2User.getAttributes() : null);
        return response;
    }
}