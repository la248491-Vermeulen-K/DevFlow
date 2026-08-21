package com.vermeulenkylian.backend.DTO;

public record AuthSessionDto(LoginResponseDto response, String refreshToken) {
}
