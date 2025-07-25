package com.khundadze.UserTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.khundadze.Balamb_Docs.dtos.UserFullResponseDto;
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
    public void toUser_shouldMapCorrectly() {
        UserRequestDto requestDto = new UserRequestDto("Luka", "password123");

        User user = mapper.toUser(requestDto);

        assertAll("Mapped User",
                () -> assertEquals("Luka", user.getUsername()),
                () -> assertEquals("password123", user.getPassword()));
    }

    @Test
    public void toUserResponseDto_shouldMapCorrectly() {
        User user = new User("Luka", "password123", GlobalRole.USER);

        UserResponseDto responseDto = mapper.toUserResponseDto(user);

        assertEquals("Luka", responseDto.username());
    }

    @Test
    public void toUserMinimalResponseDto_shouldMapCorrectly() {
        User user = new User("Luka", "password123", GlobalRole.USER);

        UserMinimalResponseDto dto = mapper.toUserMinimalResponseDto(user);

        assertEquals("Luka", dto.username());
    }

    @Test
    public void toUserFullResponseDto_shouldMapCorrectly() {
        LocalDateTime now = LocalDateTime.now();
        User user = new User("Luka", "password123", GlobalRole.USER);
        user.setId(1L);
        user.setCreatedAt(now);
        user.setPermissions(List.of()); // If necessary

        UserFullResponseDto dto = mapper.toUserFullResponseDto(user);

        assertAll("Mapped UserFullResponseDto",
                () -> assertEquals("Luka", dto.username()),
                () -> assertEquals(GlobalRole.USER, dto.globalRole()),
                () -> assertEquals(now, dto.createdAt()),
                () -> assertEquals(1L, dto.id()));
    }
}
