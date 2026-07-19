package com.balmik.dpgs.service.impl;

import com.balmik.dpgs.dto.request.LoginRequest;
import com.balmik.dpgs.dto.request.RegisterRequest;
import com.balmik.dpgs.dto.response.AuthResponse;
import com.balmik.dpgs.entity.User;
import com.balmik.dpgs.enums.Role;
import com.balmik.dpgs.exception.EmailAlreadyExistsException;
import com.balmik.dpgs.exception.InvalidCredentialsException;
import com.balmik.dpgs.repository.UserRepository;
import com.balmik.dpgs.security.JwtUtil;
import com.balmik.dpgs.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public void register(RegisterRequest request) {

        log.info("User registration request received. Email={}", request.getEmail());

        if(userRepository.existsByEmail(request.getEmail())){
            log.warn("Registration failed. Email already exists. Email={}", request.getEmail());
            throw new EmailAlreadyExistsException("Email already exists");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);
        log.info("User registered successfully. Email={}", user.getEmail());
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        log.info("Login request received. Email={}", request.getEmail());
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid Credentials"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            log.warn("Invalid login attempt. Email={}", request.getEmail());
            throw new InvalidCredentialsException("Invalid Credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        log.info("Login successful. Email={}", request.getEmail());
        return new AuthResponse(token, "Bearer");
    }
}
