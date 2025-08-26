package com.krainet.auth.service.serviceImpl;

import com.krainet.auth.dto.NotificationRequest;
import com.krainet.auth.dto.RegisterRequest;
import com.krainet.auth.model.User;
import com.krainet.auth.repository.UserRepository;
import com.krainet.auth.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificationClientImpl notificationClient;

    @Override
    public User register(RegisterRequest registerRequest, boolean initiatedByAdmin) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        User user = User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .email(registerRequest.getEmail())
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .role(User.Role.USER)
                .build();
        userRepository.save(user);

        if (!initiatedByAdmin) {
            List<String> adminEmails = userRepository.findAllByRole(User.Role.ADMIN).stream().map(User::getEmail).toList();
            NotificationRequest notificationRequest = new NotificationRequest(
                    user.getUsername(),
                    user.getEmail(),
                    user.getPassword(),
                    "Создал",
                    adminEmails
            );
            notificationClient.sendNotification(notificationRequest);
        }
        return user;
    }
}
