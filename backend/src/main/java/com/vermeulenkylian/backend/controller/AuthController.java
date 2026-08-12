package com.vermeulenkylian.backend.controller;

import com.vermeulenkylian.backend.DTO.RegisterRequestDto;
import com.vermeulenkylian.backend.DTO.UserResponseDto;
import com.vermeulenkylian.backend.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/register")
    public UserResponseDto registerUser(@RequestBody RegisterRequestDto registerRequestDto) {
        return userService.register(registerRequestDto);
    }
}
