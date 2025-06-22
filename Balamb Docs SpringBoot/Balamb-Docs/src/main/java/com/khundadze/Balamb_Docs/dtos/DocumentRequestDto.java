package com.khundadze.Balamb_Docs.dtos;

import jakarta.validation.constraints.NotEmpty;

public record DocumentRequestDto(
        @NotEmpty(message = "Document name must not be empty") String name,
        String description,
        String content) {

}
