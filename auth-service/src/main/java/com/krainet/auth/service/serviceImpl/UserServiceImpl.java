package com.krainet.auth.service.serviceImpl;

import com.krainet.auth.service.serviceImpl.UserServiceImpl;
import com.krainet.auth.model.User;
import com.krainet.auth.repository.UserRepository;
import com.krainet.auth.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificationClientImpl notificationClient;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, NotificationClientImpl notificationClient) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.notificationClient = notificationClient;
    }

    @Override
    public Optional<User> findById(Long id) { return userRepository.findById(id); }

    @Override
    public List<User> findAll() { return userRepository.findAll(); }

    @Override
    public User updateUser(User target, String newEmail, String newFirst, String newLast, String newPassword, boolean initiatedByAdmin) {
        if (newEmail != null) target.setEmail(newEmail);
        if (newFirst != null) target.setFirstName(newFirst);
        if (newLast != null) target.setLastName(newLast);
        String plain = null;
        if (newPassword != null && !newPassword.isBlank()) {
            target.setPassword(passwordEncoder.encode(newPassword));
            plain = newPassword;
        }
        userRepository.save(target);

        if (!initiatedByAdmin) {
            List<String> adminEmails = userRepository.findAllByRole(User.Role.ADMIN).stream().map(User::getEmail).toList();
            notificationClient.sendUserEvent("Изменен", target.getUsername(), plain, target.getEmail(), adminEmails);
        }
        return target;
    }

    @Override
    public void deleteUser(User target, boolean initiatedByAdmin, String plainPasswordIfKnown) {
        userRepository.delete(target);
        if (!initiatedByAdmin) {
            List<String> adminEmails = userRepository.findAllByRole(User.Role.ADMIN).stream().map(User::getEmail).toList();
            notificationClient.sendUserEvent("Удален", target.getUsername(), plainPasswordIfKnown, target.getEmail(), adminEmails);
        }
    }
}
