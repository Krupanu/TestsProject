package com.krainet.auth.repository;

import com.krainet.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findAllByRole(User.Role role);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
