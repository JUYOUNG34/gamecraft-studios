// backend/src/main/java/com/gamecraft/studios/config/SecurityConfig.java
package com.gamecraft.studios.config;

import com.gamecraft.studios.config.OAuth2SuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfig = new org.springframework.web.cors.CorsConfiguration();
                    corsConfig.setAllowedOriginPatterns(java.util.List.of(
                            "http://localhost:3000",
                            "http://127.0.0.1:3000",
                            "https://*.vercel.app",
                            "https://*.netlify.app"
                    ));
                    corsConfig.setAllowedMethods(java.util.List.of(
                            "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
                    ));
                    corsConfig.setAllowedHeaders(java.util.List.of("*"));
                    corsConfig.setAllowCredentials(true);
                    corsConfig.setMaxAge(3600L);
                    corsConfig.setExposedHeaders(java.util.List.of(
                            "Access-Control-Allow-Origin",
                            "Access-Control-Allow-Credentials"
                    ));
                    return corsConfig;
                }))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // 공개 API 엔드포인트
                        .requestMatchers("/", "/hello", "/api-status", "/api-guide").permitAll()
                        .requestMatchers("/hello-page", "/login", "/oauth-test").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()

                        // 인증 관련 엔드포인트
                        .requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll()
                        .requestMatchers("/auth/kakao/login-url").permitAll()

                        // 관리자 엔드포인트
                        .requestMatchers("/admin/**").authenticated()

                        // 사용자 엔드포인트
                        .requestMatchers("/auth/kakao/user-info").authenticated()
                        .requestMatchers("/application/**").authenticated()

                        // 나머지는 인증 필요
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oAuth2SuccessHandler)
                        .failureUrl("http://localhost:3000/auth/login?error=oauth_failed")
                        .authorizationEndpoint(authz -> authz
                                .baseUri("/oauth2/authorization")
                        )
                        .redirectionEndpoint(redirect -> redirect
                                .baseUri("/login/oauth2/code/*")
                        )
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("http://localhost:3000")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                )
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.disable())
                );

        return http.build();
    }
}