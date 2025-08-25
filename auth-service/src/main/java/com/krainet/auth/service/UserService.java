package com.krainet.auth.service;

import com.krainet.auth.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> findById(Long id);
    List<User> findAll();
    User updateUser(User target, String newEmail, String newFirst, String newLast, String newPassword, boolean initiatedByAdmin);
    void deleteUser(User target, boolean initiatedByAdmin, String plainPasswordIfKnown);
}
