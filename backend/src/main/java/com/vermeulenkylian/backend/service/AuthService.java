package com.vermeulenkylian.backend.service;

import com.vermeulenkylian.backend.DTO.LoginRequestDto;
import com.vermeulenkylian.backend.DTO.LoginResponseDto;
import com.vermeulenkylian.backend.DTO.RegisterRequestDto;
import com.vermeulenkylian.backend.DTO.UserResponseDto;
import com.vermeulenkylian.backend.model.User;
import com.vermeulenkylian.backend.repository.UserRepository;
import com.vermeulenkylian.backend.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
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

    public LoginResponseDto login(LoginRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new RuntimeException("Email ou mot de passe incorrect"));
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Email ou mot de passe incorrect");
        }
        return new LoginResponseDto(jwtService.generateToken(user), user.getId(), user.getName(), user.getEmail());
    }
}