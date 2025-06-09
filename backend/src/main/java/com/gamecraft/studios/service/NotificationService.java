package com.gamecraft.studios.service;

import com.gamecraft.studios.entity.Notification;
import com.gamecraft.studios.entity.User;
import com.gamecraft.studios.repository.NotificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification createNotification(User user, String title, String message,
                                           Notification.Type type) {
        Notification notification = new Notification(user, title, message, type);
        return notificationRepository.save(notification);
    }

    public Notification createNotificationWithAction(User user, String title, String message,
                                                     Notification.Type type, String actionUrl,
                                                     Long relatedEntityId) {
        Notification notification = new Notification(user, title, message, type);
        notification.setActionUrl(actionUrl);
        notification.setRelatedEntityId(relatedEntityId);
        return notificationRepository.save(notification);
    }

    public Page<Notification> getUserNotifications(Long userId, Pageable pageable) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);
    }

    public long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    public Optional<Notification> markAsRead(Long notificationId) {
        Optional<Notification> notification = notificationRepository.findById(notificationId);
        if (notification.isPresent()) {
            notification.get().markAsRead();
            notificationRepository.save(notification.get());
        }
        return notification;
    }

    public int markAllAsRead(Long userId) {
        return notificationRepository.markAllAsReadByUserId(userId, LocalDateTime.now());
    }

    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    // 자동 알림 생성 메서드들
    public void notifyApplicationStatusChange(User user, String company, String position, String newStatus) {
        String title = "지원서 상태 변경";
        String message = String.format("%s %s 지원서 상태가 '%s'로 변경되었습니다.",
                company, position, newStatus);
        createNotification(user, title, message, Notification.Type.APPLICATION_STATUS);
    }

    public void notifyNewJobPosting(User user, String companyName, String jobTitle) {
        String title = "새로운 채용공고";
        String message = String.format("%s에서 '%s' 포지션 채용공고가 등록되었습니다.",
                companyName, jobTitle);
        createNotification(user, title, message, Notification.Type.NEW_JOB);
    }

    public void notifyInterviewScheduled(User user, String company, String position, String interviewDate) {
        String title = "면접 일정 안내";
        String message = String.format("%s %s 포지션 면접이 %s에 예정되어 있습니다.",
                company, position, interviewDate);
        createNotification(user, title, message, Notification.Type.INTERVIEW_SCHEDULED);
    }

    // 오래된 알림 정리 (스케줄러에서 호출)
    public int cleanupOldNotifications(int daysOld) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysOld);
        return notificationRepository.deleteOldNotifications(cutoffDate);
    }
}
