package com.vermeulenkylian.backend.service;

import com.vermeulenkylian.backend.DTO.RegisterRequestDto;
import com.vermeulenkylian.backend.DTO.UserResponseDto;
import com.vermeulenkylian.backend.model.User;
import com.vermeulenkylian.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDto register(RegisterRequestDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Cet email est déjà utilisé");
        }
        String password = passwordEncoder.encode(dto.getPassword());
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(password);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
        return new UserResponseDto(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt());
    }
}   