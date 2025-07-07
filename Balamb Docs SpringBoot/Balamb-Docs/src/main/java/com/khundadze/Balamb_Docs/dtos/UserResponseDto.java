package com.khundadze.Balamb_Docs.dtos;

import com.khundadze.Balamb_Docs.models.GlobalRole;

public record UserResponseDto(
                Long id,
                String username,
                GlobalRole globalRole) {

}
