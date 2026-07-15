package com.balmik.dpgs.dto.response;

import com.balmik.dpgs.enums.NotificationStatus;
import com.balmik.dpgs.enums.NotificationType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationResponse {
    private NotificationType type;
    private NotificationStatus status;
    private String subject;
    private String message;
    private LocalDateTime createdAt;
}
