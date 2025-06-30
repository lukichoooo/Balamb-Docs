package com.khundadze.Balamb_Docs.services;

import org.springframework.stereotype.Service;

import com.khundadze.Balamb_Docs.dtos.UserMinimalResponseDto;
import com.khundadze.Balamb_Docs.dtos.UserRequestDto;
import com.khundadze.Balamb_Docs.dtos.UserResponseDto;
import com.khundadze.Balamb_Docs.models.GlobalRole;
import com.khundadze.Balamb_Docs.models.User;

@Service
public class UserMapper {

    public User toUser(UserRequestDto requestUser) {
        return new User(requestUser.username(),
                requestUser.email(),
                requestUser.password(),
                GlobalRole.USER);
    }

    public UserResponseDto toUserResponseDto(User user) {
        return new UserResponseDto(user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getGlobalRole());
    }

    public UserMinimalResponseDto toUserMinimalResponseDto(User user) {
        return new UserMinimalResponseDto(user.getId(), user.getUsername());
    }
}
