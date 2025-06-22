package com.khundadze.UserTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.khundadze.Balamb_Docs.controllers.UserController;
import com.khundadze.Balamb_Docs.dtos.UserRequestDto;
import com.khundadze.Balamb_Docs.dtos.UserResponseDto;
import com.khundadze.Balamb_Docs.models.GlobalRole;
import com.khundadze.Balamb_Docs.services.UserService;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void save() {
        UserRequestDto requestDto = new UserRequestDto("Luka", "luka@example.com", "password123");
        UserResponseDto responseDto = new UserResponseDto(1L, "Luka", "luka@example.com", GlobalRole.USER);

        when(userService.save(requestDto)).thenReturn(responseDto);

        UserResponseDto result = userController.save(requestDto);
        assertEquals(result, responseDto);
    }

    @Test
    public void findById() {
        Long id = 1L;
        UserResponseDto responseDto = new UserResponseDto(1L, "Luka", "luka@example.com", GlobalRole.USER);

        when(userService.findById(id)).thenReturn(responseDto);

        UserResponseDto result = userController.findById(id);
        assertEquals(result, responseDto);
    }

    @Test
    public void findByName() {
        String name = "Luka";
        UserResponseDto responseDto = new UserResponseDto(1L, "Luka", "luka@example.com", GlobalRole.USER);

        when(userService.findByName(name)).thenReturn(responseDto);

        UserResponseDto result = userController.findByName(name);
        assertEquals(result, responseDto);
    }
}
