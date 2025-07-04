package com.khundadze.UserTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.khundadze.Balamb_Docs.dtos.UserMinimalResponseDto;
import com.khundadze.Balamb_Docs.dtos.UserRequestDto;
import com.khundadze.Balamb_Docs.dtos.UserResponseDto;
import com.khundadze.Balamb_Docs.models.GlobalRole;
import com.khundadze.Balamb_Docs.models.User;
import com.khundadze.Balamb_Docs.services.UserMapper;

public class UserMapperTest {

    private UserMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new UserMapper();
    }

    @Test
    public void toUser() {
        UserRequestDto requestDto = new UserRequestDto("Luka", "luka@example.com", "password123");
        User user = mapper.toUser(requestDto);
        assertEquals(user.getUsername(), "Luka");
        assertEquals(user.getEmail(), "luka@example.com");
        assertEquals(user.getPassword(), "password123");
    }

    @Test
    public void toUserResponseDto() {
        User user = new User("Luka", "luka@example.com", "password123", GlobalRole.USER);
        UserResponseDto responseDto = mapper.toUserResponseDto(user);
        assertEquals(responseDto.username(), "Luka");
        assertEquals(responseDto.email(), "luka@example.com");
    }

    @Test
    public void toUserMinimalResponseDto() {
        User user = new User("Luka", "luka@example.com", "password123", GlobalRole.USER);
        UserMinimalResponseDto minimalResponseDto = mapper.toUserMinimalResponseDto(user);
        assertEquals(minimalResponseDto.username(), "Luka");
    }
}
