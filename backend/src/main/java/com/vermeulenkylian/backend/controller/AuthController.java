package com.vermeulenkylian.backend.controller;

import com.vermeulenkylian.backend.DTO.*;
import com.vermeulenkylian.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final boolean secureCookies;
    private final long refreshCookieMaxAge;

    public AuthController(
            AuthService authService,
            @Value("${app.auth.cookie-secure}") boolean secureCookies,
            @Value("${app.jwt.refresh-expiration-days}") long refreshExpirationDays
    ) {
        this.authService = authService;
        this.secureCookies = secureCookies;
        this.refreshCookieMaxAge = Duration.ofDays(refreshExpirationDays).getSeconds();
    }
    @PostMapping("/register")
    public UserResponseDto registerUser(@RequestBody RegisterRequestDto registerRequestDto) {
        return authService.register(registerRequestDto);
    }
    @PostMapping("/login")
    public LoginResponseDto loginUser(@RequestBody LoginRequestDto loginRequestDto, jakarta.servlet.http.HttpServletResponse response) {
        AuthSessionDto session = authService.login(loginRequestDto);
        addRefreshCookie(response, session.refreshToken(), refreshCookieMaxAge);
        return session.response();
    }
    @PostMapping("/refresh")
    public RefreshResponseDto refreshToken(
            @CookieValue(value = "refresh_token", required = false) String refreshToken,
            jakarta.servlet.http.HttpServletResponse response
    ) {
        AuthSessionDto session = authService.refreshToken(refreshToken);
        addRefreshCookie(response, session.refreshToken(), refreshCookieMaxAge);
        return new RefreshResponseDto(session.response().getToken());
    }

    @PostMapping("/logout")
    public void logout(
            @CookieValue(value = "refresh_token", required = false) String refreshToken,
            jakarta.servlet.http.HttpServletResponse response
    ) {
        authService.logout(refreshToken);
        addRefreshCookie(response, "", 0);
    }

    private void addRefreshCookie(jakarta.servlet.http.HttpServletResponse response, String token, long maxAge) {
        ResponseCookie cookie = ResponseCookie.from("refresh_token", token)
                .httpOnly(true)
                .secure(secureCookies)
                .sameSite("Strict")
                .path("/api/auth")
                .maxAge(maxAge)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
