package com.gamecraft.studios.controller;

import com.gamecraft.studios.entity.Notification;
import com.gamecraft.studios.entity.User;
import com.gamecraft.studios.service.NotificationService;
import com.gamecraft.studios.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    public NotificationController(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getNotifications(
            @AuthenticationPrincipal OAuth2User oauth2User,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Map<String, Object> response = new HashMap<>();

        try {
            if (oauth2User == null) {
                response.put("success", false);
                response.put("message", "로그인이 필요합니다");
                return ResponseEntity.unauthorized().body(response);
            }

            User user = userService.getCurrentUser(oauth2User);
            Pageable pageable = PageRequest.of(page, size);
            Page<Notification> notifications = notificationService.getUserNotifications(user.getId(), pageable);

            response.put("success", true);
            response.put("notifications", notifications.getContent());
            response.put("pagination", Map.of(
                    "currentPage", notifications.getNumber(),
                    "totalPages", notifications.getTotalPages(),
                    "totalElements", notifications.getTotalElements(),
                    "hasNext", notifications.hasNext(),
                    "hasPrevious", notifications.hasPrevious()
            ));
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "알림 조회 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/unread")
    public ResponseEntity<Map<String, Object>> getUnreadNotifications(@AuthenticationPrincipal OAuth2User oauth2User) {
        Map<String, Object> response = new HashMap<>();

        try {
            if (oauth2User == null) {
                response.put("success", false);
                response.put("message", "로그인이 필요합니다");
                return ResponseEntity.unauthorized().body(response);
            }

            User user = userService.getCurrentUser(oauth2User);
            List<Notification> unreadNotifications = notificationService.getUnreadNotifications(user.getId());
            long unreadCount = notificationService.getUnreadCount(user.getId());

            response.put("success", true);
            response.put("notifications", unreadNotifications);
            response.put("unreadCount", unreadCount);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "읽지 않은 알림 조회 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Map<String, Object>> markAsRead(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            Optional<Notification> notification = notificationService.markAsRead(id);
            if (notification.isPresent()) {
                response.put("success", true);
                response.put("message", "알림을 읽음 처리했습니다");
                response.put("notification", notification.get());
            } else {
                response.put("success", false);
                response.put("message", "알림을 찾을 수 없습니다");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "알림 읽음 처리 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @PutMapping("/read-all")
    public ResponseEntity<Map<String, Object>> markAllAsRead(@AuthenticationPrincipal OAuth2User oauth2User) {
        Map<String, Object> response = new HashMap<>();

        try {
            if (oauth2User == null) {
                response.put("success", false);
                response.put("message", "로그인이 필요합니다");
                return ResponseEntity.unauthorized().body(response);
            }

            User user = userService.getCurrentUser(oauth2User);
            int updatedCount = notificationService.markAllAsRead(user.getId());

            response.put("success", true);
            response.put("message", "모든 알림을 읽음 처리했습니다");
            response.put("updatedCount", updatedCount);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "전체 읽음 처리 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }
}