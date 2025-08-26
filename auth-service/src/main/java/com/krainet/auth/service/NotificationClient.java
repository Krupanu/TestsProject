package com.krainet.auth.service;

import com.krainet.auth.dto.NotificationRequest;

public interface NotificationClient {
    void sendNotification(NotificationRequest request);
}
