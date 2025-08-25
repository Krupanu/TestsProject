package com.krainet.notification.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class UserEventNotificationRequest {
    @NotBlank
    private String action; // Created/Updated/Deleted (in Russian could be Создан/Изменен/Удален)
    @NotBlank
    private String username;
    private String password;

    @Email
    private String email;

    @NotNull
    private List<@Email String> adminEmails;
}
