package com.vermeulenkylian.backend.controller;

import com.vermeulenkylian.backend.DTO.UserProfileDto;
import com.vermeulenkylian.backend.model.User;
import com.vermeulenkylian.backend.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
