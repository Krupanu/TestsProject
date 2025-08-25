package com.krainet.auth.service.serviceImpl;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationClientImpl {

    private final RestTemplate restTemplate;
    private final String notificationUrl = "http://notification-service:8081/api/notifications";

    public NotificationClientImpl(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public void sendNotification(NotificationRequest request) {
        restTemplate.postForEntity(notificationUrl, request, Void.class);
    }
}