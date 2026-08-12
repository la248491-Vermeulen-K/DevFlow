package com.vermeulenkylian.backend.controller;

import com.vermeulenkylian.backend.DTO.LoginRequestDto;
import com.vermeulenkylian.backend.DTO.LoginResponseDto;
import com.vermeulenkylian.backend.DTO.RegisterRequestDto;
import com.vermeulenkylian.backend.DTO.UserResponseDto;
import com.vermeulenkylian.backend.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
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
}
