package com.khundadze.Balamb_Docs.dtos;

import java.time.LocalDateTime;

public record DocumentFullInfoResponseDto(
        Long id,
        String name,
        boolean isPublic,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
