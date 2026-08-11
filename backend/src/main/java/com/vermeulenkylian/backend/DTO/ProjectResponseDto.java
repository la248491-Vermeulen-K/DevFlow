package com.vermeulenkylian.backend.DTO;

import java.time.LocalDateTime;

public record ProjectResponseDto(
        Long id,
        String name,
        String description,
        LocalDateTime createdAt
) {
}
