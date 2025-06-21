package com.khundadze.Balamb_Docs.services;

import org.springframework.stereotype.Service;

import com.khundadze.Balamb_Docs.dtos.UserRequestDto;
import com.khundadze.Balamb_Docs.dtos.UserResponseDto;
import com.khundadze.Balamb_Docs.models.User;

@Service
public class UserMapper {

    public User toUser(UserRequestDto requestUser) {
        return new User(requestUser.name(), requestUser.email(), requestUser.password(),
                ""); // TODO: add role
    }

    public UserResponseDto toUserResponseDto(User user) {
        return new UserResponseDto(user.getName(), user.getEmail());
    }
}
