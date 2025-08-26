package com.krainet.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {
    private String username;
    private String email;
    private String password;
    private String action;
    private List<String> adminEmails;
}

