package com.krainet.auth.service.serviceImpl;

import com.krainet.auth.dto.NotificationRequest;
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
    public User updateUser(User user, String newEmail, String newFirst, String newLast, String newPassword, boolean initiatedByAdmin) {
        if (newEmail != null) user.setEmail(newEmail);
        if (newFirst != null) user.setFirstName(newFirst);
        if (newLast != null) user.setLastName(newLast);
        String plain = null;
        if (newPassword != null && !newPassword.isBlank()) {
            user.setPassword(passwordEncoder.encode(newPassword));
            plain = newPassword;
        }
        userRepository.save(user);

        if (!initiatedByAdmin) {
            List<String> adminEmails = userRepository.findAllByRole(User.Role.ADMIN).stream().map(User::getEmail).toList();
            NotificationRequest notificationRequest = new NotificationRequest(
                    user.getUsername(),
                    user.getEmail(),
                    user.getPassword(),
                    "Изменен",
                    adminEmails
            );
            notificationClient.sendNotification(notificationRequest);
        }
        return user;
    }

    @Override
    public void deleteUser(User user, boolean initiatedByAdmin, String plainPasswordIfKnown) {
        userRepository.delete(user);
        if (!initiatedByAdmin) {
            List<String> adminEmails = userRepository.findAllByRole(User.Role.ADMIN).stream().map(User::getEmail).toList();
            NotificationRequest notificationRequest = new NotificationRequest(
                    user.getUsername(),
                    user.getEmail(),
                    user.getPassword(),
                    "Удален",
                    adminEmails
            );
            notificationClient.sendNotification(notificationRequest);
        }
    }
}
