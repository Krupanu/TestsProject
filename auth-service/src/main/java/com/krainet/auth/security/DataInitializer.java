package com.krainet.auth.security;

import com.krainet.auth.model.User;
import com.krainet.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("admin").isEmpty()) {
            userRepository.save(new User(
                    Long.valueOf("1"),
                    "admin",
                    passwordEncoder.encode("admin123"),
                    "Krupanu2@gmail.com",
                    "Admin",
                    "User",
                    User.Role.ADMIN
            ));
        }
        if (userRepository.findByUsername("user").isEmpty()) {
            userRepository.save(new User(
                    Long.valueOf("2"),
                    "user",
                    passwordEncoder.encode("user123"),
                    "user@example.com",
                    "User",
                    "User",
                    User.Role.USER
            ));
        }
    }
}
