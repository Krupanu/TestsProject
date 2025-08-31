package com.krainet.auth.controller;

import com.krainet.auth.dto.RegisterRequest;
import com.krainet.auth.dto.UserResponse;
import com.krainet.auth.service.serviceImpl.UserServiceImpl;
import com.krainet.auth.model.User;
import com.krainet.auth.repository.UserRepository;
import com.krainet.auth.service.serviceImpl.RegistrationServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {

    private final RegistrationServiceImpl registrationServiceImpl;
    private final UserRepository userRepository;
    private final UserServiceImpl userServiceImpl;


    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")

    // WORKING

    public List<UserResponse> all() {
        return userRepository.findAll().stream().map(UserResponse::from).toList();
    }

    @GetMapping("/me")

    //WORKING

    public UserResponse me(Principal principal) {

        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        return UserResponse.from(user);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse byId(@PathVariable("id") Long id) {
        return userRepository.findById(id).map(UserResponse::from).orElseThrow();
    }

    @PostMapping("/create/by/admin")
    @PreAuthorize("hasRole('ADMIN')")

    //WORKING

    public ResponseEntity<UserResponse> createByAdmin(@RequestBody @Valid RegisterRequest request) {
        User user = registrationServiceImpl.register(request, true);
        return ResponseEntity.ok(UserResponse.from(user));
    }

    @PutMapping("/{id}")
    public UserResponse update(@PathVariable("id") Long id, @RequestBody UpdateUserRequest req, Principal principal) {
        User target = userRepository.findById(id).orElseThrow();
        boolean isAdmin = userRepository.findByUsername(principal.getName()).orElseThrow().getRole() == User.Role.ADMIN;
        if (!isAdmin && !principal.getName().equals(target.getUsername())) {
            throw new RuntimeException("Forbidden");
        }
        User user = userServiceImpl.updateUser(target, req.email, req.firstName, req.lastName, req.password, isAdmin);
        return UserResponse.from(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id, Principal principal) {
        User target = userRepository.findById(id).orElseThrow();
        boolean isAdmin = userRepository.findByUsername(principal.getName()).orElseThrow().getRole() == User.Role.ADMIN;
        if (!isAdmin && !principal.getName().equals(target.getUsername())) {
            throw new RuntimeException("Forbidden");
        }
        userServiceImpl.deleteUser(target, isAdmin, null);
        return ResponseEntity.noContent().build();
    }

    public record UpdateUserRequest(String email, String firstName, String lastName, String password) {}
}
