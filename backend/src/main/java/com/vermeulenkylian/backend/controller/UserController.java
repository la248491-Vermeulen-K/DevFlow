package com.vermeulenkylian.backend.controller;

import com.vermeulenkylian.backend.DTO.UpdateProfileRequestDto;
import com.vermeulenkylian.backend.DTO.UserProfileDto;
import com.vermeulenkylian.backend.model.User;
import com.vermeulenkylian.backend.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public UserProfileDto me(@AuthenticationPrincipal User user) {
        return userService.getProfile(user);
    }
    @PutMapping("/me")
    public UserProfileDto updateProfile(@AuthenticationPrincipal User user, @RequestBody UpdateProfileRequestDto dto) {
        return userService.updateProfile(user, dto);
    }
}
