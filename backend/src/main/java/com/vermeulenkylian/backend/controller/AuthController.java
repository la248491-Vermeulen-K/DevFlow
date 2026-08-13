package com.vermeulenkylian.backend.controller;

import com.vermeulenkylian.backend.DTO.*;
import com.vermeulenkylian.backend.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("/register")
    public UserResponseDto registerUser(@RequestBody RegisterRequestDto registerRequestDto) {
        return authService.register(registerRequestDto);
    }
    @PostMapping("/login")
    public LoginResponseDto loginUser(@RequestBody LoginRequestDto loginRequestDto) {
        return authService.login(loginRequestDto);
    }
    @PostMapping("/refresh")
    public String refreshToken(@RequestBody RefreshRequestDto refreshToken) {
        return authService.refreshToken(refreshToken);
    }
}
