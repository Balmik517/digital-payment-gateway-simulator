package com.balmik.dpgs.service.impl;

import com.balmik.dpgs.dto.response.NotificationResponse;
import com.balmik.dpgs.entity.Notification;
import com.balmik.dpgs.entity.User;
import com.balmik.dpgs.repository.NotificationRepository;
import com.balmik.dpgs.repository.UserRepository;
import com.balmik.dpgs.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    public List<NotificationResponse> getMyNotifications(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(
                ()-> new RuntimeException("User not found"));

        return notificationRepository.findByUser(user).stream().map(this::mapToResponse).toList();
    }


    private NotificationResponse mapToResponse(Notification notification){

        return NotificationResponse.builder()
                .type(notification.getType())
                .status(notification.getStatus())
                .subject(notification.getSubject())
                .message(notification.getMessage())
                .createdAt(notification.getCreatedAt()).build();
    }
}
