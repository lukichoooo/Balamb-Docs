package com.khundadze.Balamb_Docs.dtos;

import java.time.LocalDateTime;

import com.khundadze.Balamb_Docs.models.GlobalRole;

public record UserFullResponseDto(
                Long id,
                String username,
                GlobalRole globalRole,
                LocalDateTime createdAt) {
}