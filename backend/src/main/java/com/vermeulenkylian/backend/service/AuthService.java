package com.vermeulenkylian.backend.service;

import com.vermeulenkylian.backend.DTO.*;
import com.vermeulenkylian.backend.exception.BadRequestException;
import com.vermeulenkylian.backend.exception.UnauthorizedException;
import com.vermeulenkylian.backend.model.RefreshToken;
import com.vermeulenkylian.backend.model.User;
import com.vermeulenkylian.backend.repository.RefreshTokenRepository;
import com.vermeulenkylian.backend.repository.UserRepository;
import com.vermeulenkylian.backend.security.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;
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

    public AuthSessionDto login(LoginRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new UnauthorizedException("Invalid email or password"));
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid email or password");
        }
        String rawRefreshToken = createRefreshToken(user);
        return new AuthSessionDto(
                new LoginResponseDto(jwtService.generateToken(user), user.getId(), user.getName(), user.getEmail()),
                rawRefreshToken
        );
    }

    @Transactional
    public AuthSessionDto refreshToken(String rawRefreshToken) {
        if (rawRefreshToken == null || rawRefreshToken.isBlank()) {
            throw new UnauthorizedException("Refresh token is missing");
        }

        RefreshToken refreshToken = refreshTokenRepository.findByToken(hashToken(rawRefreshToken))
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));
        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new UnauthorizedException("Refresh token has expired");
        }

        User user = refreshToken.getUser();
        refreshTokenRepository.delete(refreshToken);
        String nextRawRefreshToken = createRefreshToken(user);
        return new AuthSessionDto(
                new LoginResponseDto(jwtService.generateToken(user), user.getId(), user.getName(), user.getEmail()),
                nextRawRefreshToken
        );
    }

    @Transactional
    public void logout(String rawRefreshToken) {
        if (rawRefreshToken != null && !rawRefreshToken.isBlank()) {
            refreshTokenRepository.findByToken(hashToken(rawRefreshToken)).ifPresent(refreshTokenRepository::delete);
        }
    }

    private String createRefreshToken(User user) {
        String rawToken = UUID.randomUUID().toString();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(hashToken(rawToken));
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(refreshExpirationDays));
        refreshTokenRepository.save(refreshToken);
        return rawToken;
    }

    private String hashToken(String token) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256")
                    .digest(token.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is unavailable", exception);
        }
    }
}
