package com.vermeulenkylian.backend.controller;

import com.vermeulenkylian.backend.DTO.UpdateProfileRequestDto;
import com.vermeulenkylian.backend.DTO.UserProfileDto;
import com.vermeulenkylian.backend.model.User;
import com.vermeulenkylian.backend.service.AvatarService;
import com.vermeulenkylian.backend.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final AvatarService avatarService;

    public UserController(UserService userService, AvatarService avatarService) {
        this.userService = userService;
        this.avatarService = avatarService;
    }

    @GetMapping("/me")
    public UserProfileDto me(@AuthenticationPrincipal User user) {
        return userService.getProfile(user);
    }
    @PutMapping("/me")

    public UserProfileDto updateProfile(@AuthenticationPrincipal User user, @RequestBody UpdateProfileRequestDto dto) {
        return userService.updateProfile(user, dto);
    }

    @PostMapping("/me/avatar")
    public UserProfileDto uploadAvatar(@AuthenticationPrincipal User user, @RequestParam("file") MultipartFile file) {
        return avatarService.uploadAvatar(user, file);
    }
}
