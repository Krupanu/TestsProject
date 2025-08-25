package com.krainet.auth.controller;

import com.krainet.auth.dto.AuthResponse;
import com.krainet.auth.dto.LoginRequest;
import com.krainet.auth.dto.RegisterRequest;
import com.krainet.auth.dto.UserResponse;
import com.krainet.auth.security.JwtService;
import com.krainet.auth.service.serviceImpl.RegistrationServiceImpl;
import com.krainet.auth.model.User;
import com.krainet.auth.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RegistrationServiceImpl registrationServiceImpl;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody @Valid RegisterRequest request) {
        User user = registrationServiceImpl.register(request, false);
        return ResponseEntity.ok(UserResponse.from(user));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest req) {
        User user = userRepository.findByUsername(req.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Bad credentials");
        }
        String token = jwtService.generateToken(user.getUsername(), user.getRole().name());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
