package com.khundadze.Balamb_Docs.services;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.khundadze.Balamb_Docs.dtos.UserFullResponseDto;
import com.khundadze.Balamb_Docs.dtos.UserMinimalResponseDto;
import com.khundadze.Balamb_Docs.dtos.UserRequestDto;
import com.khundadze.Balamb_Docs.dtos.UserResponseDto;
import com.khundadze.Balamb_Docs.exceptions.UserNotFoundException;
import com.khundadze.Balamb_Docs.models.User;
import com.khundadze.Balamb_Docs.repositories.IUserRepository;

@Service
public class UserService {
    private final IUserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(IUserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDto save(UserRequestDto requestUser) {
        User user = userMapper.toUser(requestUser);

        user.setPassword(passwordEncoder.encode(requestUser.password()));

        return userMapper.toUserResponseDto(userRepository.save(user));
    }

    public UserResponseDto findByUsername(String name) {
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new UserNotFoundException("User not found with name: " + name));
        return userMapper.toUserResponseDto(user);
    }

    public List<UserMinimalResponseDto> findByUsernameLike(String name) {
        List<User> users = userRepository.findTop5ByUsernameStartsWithIgnoreCase(name);
        if (users.isEmpty()) {
            throw new UserNotFoundException("User not found with name: " + name);
        }
        return users.stream()
                .map(userMapper::toUserMinimalResponseDto)
                .toList();
    }

    public UserResponseDto findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return userMapper.toUserResponseDto(user);
    }

    public UserFullResponseDto findFullById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return userMapper.toUserFullResponseDto(user);
    }
}
