package com.balmik.dpgs.service;

import com.balmik.dpgs.dto.response.NotificationResponse;

import java.util.List;

public interface NotificationService {
    List<NotificationResponse> getMyNotifications(String email);
}
