package com.krainet.auth.service.serviceImpl;

import com.krainet.auth.dto.RegisterRequest;
import com.krainet.auth.model.User;
import com.krainet.auth.repository.UserRepository;
import com.krainet.auth.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificationClientImpl notificationClient;

    @Transactional
    public User register(RegisterRequest request, boolean initiatedByAdmin) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(User.Role.USER)
                .build();
        userRepository.save(user);

        if (!initiatedByAdmin) {
            List<String> adminEmails = userRepository.findAllByRole(User.Role.ADMIN).stream().map(User::getEmail).toList();
            notificationClient.sendUserEvent("Создан", user.getUsername(), request.getPassword(), user.getEmail(), adminEmails);
        }
        return user;
    }
}
