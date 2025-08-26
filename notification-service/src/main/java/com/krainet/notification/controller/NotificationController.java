package com.krainet.notification.controller;

import com.krainet.notification.model.UserEventNotificationRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String from;

    @PostMapping("/user-event")
    public void notifyAdmins(@RequestBody @Valid UserEventNotificationRequest request) {
        String subject = String.format("%s пользователь %s.", request.getAction(), request.getUsername());
        String text = String.format("%s пользователь с именем - %s, паролем - %s и почтой - %s.",
                request.getAction(), request.getUsername(), request.getPassword(), request.getEmail());

        for (String adminEmail : request.getAdminEmails()) {
            try {
                SimpleMailMessage msg = new SimpleMailMessage();
                msg.setFrom(from);
                msg.setTo(adminEmail);
                msg.setSubject(subject);
                msg.setText(text);
                mailSender.send(msg);
            } catch (Exception e) {
                log.error("Failed to send to {}: {}", adminEmail, e.getMessage());
            }
        }
        log.info("Sent {} notifications to admins", request.getAdminEmails().size());
    }
}
