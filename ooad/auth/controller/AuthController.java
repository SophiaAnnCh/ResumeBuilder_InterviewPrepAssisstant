package com.project.ooad.auth.controller;

import com.project.ooad.auth.dto.RegisterRequest;
import com.project.ooad.auth.dto.LoginRequest;
import com.project.ooad.auth.dto.AuthResponse;
import com.project.ooad.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}