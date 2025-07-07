package com.khundadze.Balamb_Docs.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserRequestDto(
                @NotEmpty(message = "Username must not be empty") String username,

                @NotEmpty(message = "Password must not be empty") @Size(min = 6, message = "Password must be at least 6 characters") String password) {
}
