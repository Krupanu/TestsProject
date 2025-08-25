package com.krainet.auth.service;

import com.krainet.auth.dto.RegisterRequest;
import com.krainet.auth.model.User;

public interface RegistrationService {
    User register(RegisterRequest registerRequest,boolean isAdmin);

}
