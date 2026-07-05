package com.balmik.dpgs.service;

import com.balmik.dpgs.dto.request.LoginRequest;
import com.balmik.dpgs.dto.request.RegisterRequest;
import com.balmik.dpgs.dto.response.AuthResponse;

public interface AuthService {
    void register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
