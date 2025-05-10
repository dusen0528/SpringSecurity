package com.nhnacademy.springsecurity.auth.service;

import com.nhnacademy.springsecurity.dto.RegisterRequest;

public interface AuthService {
    void register(RegisterRequest request);
}
