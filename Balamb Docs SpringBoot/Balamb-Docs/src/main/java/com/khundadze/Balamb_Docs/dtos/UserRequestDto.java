package com.khundadze.Balamb_Docs.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserRequestDto(
                @NotEmpty(message = "Name must not be empty") String name,

                @NotEmpty(message = "Email must not be empty") @Email(message = "Email must be valid") String email,

                @NotEmpty(message = "Password must not be empty") @Size(min = 6, message = "Password must be at least 6 characters") String password) {
}
