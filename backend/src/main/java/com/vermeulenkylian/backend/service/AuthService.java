package com.vermeulenkylian.backend.service;

import com.vermeulenkylian.backend.DTO.*;
import com.vermeulenkylian.backend.exception.BadRequestException;
import com.vermeulenkylian.backend.exception.NotFoundException;
import com.vermeulenkylian.backend.exception.UnauthorizedException;
import com.vermeulenkylian.backend.model.RefreshToken;
import com.vermeulenkylian.backend.model.User;
import com.vermeulenkylian.backend.repository.RefreshTokenRepository;
import com.vermeulenkylian.backend.repository.UserRepository;
import com.vermeulenkylian.backend.security.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    @Value("${app.jwt.refresh-expiration-days}") int refreshExpirationDays;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public UserResponseDto register(RegisterRequestDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new BadRequestException("This email is already in use");
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
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new BadRequestException("Email ou mot de passe incorrect"));
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BadRequestException("Email ou mot de passe incorrect");
        }
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(refreshExpirationDays));
        refreshTokenRepository.save(refreshToken);
        return new LoginResponseDto(jwtService.generateToken(user), user.getId(), user.getName(), user.getEmail(), refreshToken.getToken());
    }

    public String refreshToken(RefreshRequestDto dto){
        RefreshToken refreshToken = refreshTokenRepository.findByToken(dto.getRefreshToken())
                .orElseThrow(() ->
                        new NotFoundException("Refresh token introuvable")
                );
        if(refreshToken.getExpiryDate().isBefore(LocalDateTime.now())){
            throw new UnauthorizedException("Refresh token has expired");
        }
        User user = refreshToken.getUser();
        return jwtService.generateToken(user);
    }
}
