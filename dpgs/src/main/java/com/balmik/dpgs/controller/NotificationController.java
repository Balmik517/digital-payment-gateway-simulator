package com.balmik.dpgs.controller;

import com.balmik.dpgs.dto.response.NotificationResponse;
import com.balmik.dpgs.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/my-notifications")
    public List<NotificationResponse> getMyNotifications(Authentication authentication){
        return notificationService.getMyNotifications(authentication.getName());
    }
}
