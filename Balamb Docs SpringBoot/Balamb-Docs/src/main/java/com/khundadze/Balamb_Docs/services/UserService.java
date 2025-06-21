package com.khundadze.Balamb_Docs.services;

import org.springframework.stereotype.Service;

import com.khundadze.Balamb_Docs.dtos.UserRequestDto;
import com.khundadze.Balamb_Docs.dtos.UserResponseDto;
import com.khundadze.Balamb_Docs.models.User;
import com.khundadze.Balamb_Docs.repositories.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserResponseDto save(UserRequestDto requestUser) {
        User user = userMapper.toUser(requestUser);
        return userMapper.toUserResponseDto(userRepository.save(user));
    }

    public UserResponseDto findByName(String name) {
        User user = userRepository.findByName(name);
        if (user == null) {
            user = new User();
        }
        return userMapper.toUserResponseDto(user);
    }

    public UserResponseDto findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return userMapper.toUserResponseDto(user);
    }

    public UserResponseDto findById(Long id) {
        User user = userRepository.findById(id).orElse(new User());
        return userMapper.toUserResponseDto(user);
    }
}
