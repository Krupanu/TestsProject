package com.krainet.auth.service.serviceImpl;

import com.krainet.auth.dto.NotificationRequest;
import com.krainet.auth.service.NotificationClient;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class NotificationClientImpl implements NotificationClient {

    private final RestTemplate restTemplate;
    private final String notificationUrl = "http://notification-service:8081/api/notifications";

    public NotificationClientImpl(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    @Override
    public void sendNotification(NotificationRequest request) {
        restTemplate.postForEntity(notificationUrl, request, Void.class);
    }
}