package com.khundadze.UserTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.khundadze.Balamb_Docs.dtos.UserMinimalResponseDto;
import com.khundadze.Balamb_Docs.dtos.UserRequestDto;
import com.khundadze.Balamb_Docs.dtos.UserResponseDto;
import com.khundadze.Balamb_Docs.exceptions.UserNotFoundException;
import com.khundadze.Balamb_Docs.models.GlobalRole;
import com.khundadze.Balamb_Docs.models.User;
import com.khundadze.Balamb_Docs.repositories.UserRepository;
import com.khundadze.Balamb_Docs.services.UserMapper;
import com.khundadze.Balamb_Docs.services.UserService;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void save() {
        UserRequestDto requestDto = new UserRequestDto("Luka", "luka@example.com", "password123");
        UserResponseDto responseDto = new UserResponseDto(1L, "Luka", "luka@example.com", GlobalRole.USER);
        User user = new User("Luka", "luka@example.com", "password123", GlobalRole.USER);

        when(userMapper.toUser(requestDto)).thenReturn(user);
        when(userMapper.toUserResponseDto(user)).thenReturn(responseDto);
        when(userRepository.save(user)).thenReturn(user);

        UserResponseDto result = userService.save(requestDto);
        assertEquals(result, responseDto);
    }

    @Test
    public void findById() {
        Long id = 1L;
        User user = new User("Luka", "luka@example.com", "password123", GlobalRole.USER);
        UserResponseDto responseDto = new UserResponseDto(1L, "Luka", "luka@example.com", GlobalRole.USER);

        when(userRepository.findById(id)).thenReturn(java.util.Optional.of(user));
        when(userMapper.toUserResponseDto(user)).thenReturn(responseDto);

        UserResponseDto result = userService.findById(id);
        assertEquals(result, responseDto);
    }

    @Test
    public void findById_notFound() {
        Long id = 1L;

        when(userRepository.findById(id)).thenReturn(java.util.Optional.empty());

        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> userService.findById(id));

        assertEquals("User not found with id: " + id, ex.getMessage());
    }

    @Test
    public void findByName() {
        String name = "Luka";
        User user = new User("Luka", "luka@example.com", "password123", GlobalRole.USER);
        UserResponseDto responseDto = new UserResponseDto(1L, "Luka", "luka@example.com", GlobalRole.USER);

        when(userRepository.findByName(name)).thenReturn(user);
        when(userMapper.toUserResponseDto(user)).thenReturn(responseDto);

        UserResponseDto result = userService.findByName(name);
        assertEquals(result, responseDto);
    }

    @Test
    public void findByName_notFound() {
        String name = "Luka";

        when(userRepository.findByName(name)).thenReturn(null);

        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> userService.findByName(name));

        assertEquals("User not found with name: " + name, ex.getMessage());
    }

    @Test
    public void findByEmail() {
        String email = "luka@example.com";
        User user = new User("Luka", "luka@example.com", "password123", GlobalRole.USER);
        UserResponseDto responseDto = new UserResponseDto(1L, "Luka", "luka@example.com", GlobalRole.USER);

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(userMapper.toUserResponseDto(user)).thenReturn(responseDto);

        UserResponseDto result = userService.findByEmail(email);
        assertEquals(result, responseDto);
    }

    @Test
    public void findByEmail_notFound() {
        String email = "luka@example.com";

        when(userRepository.findByEmail(email)).thenReturn(null);

        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> userService.findByEmail(email));

        assertEquals("User not found with email: " + email, ex.getMessage());
    }

    @Test
    public void findNameLike() {
        String name = "Luka";
        User user1 = new User("Luka", "luka@example.com", "password123", GlobalRole.USER);
        User user2 = new User("Luka_2", "luka_2@example.com", "password123", GlobalRole.USER);

        UserMinimalResponseDto responseDto1 = new UserMinimalResponseDto(1L, "Luka");
        UserMinimalResponseDto responseDto2 = new UserMinimalResponseDto(2L, "Luka_2");

        when(userRepository.findTop5ByNameStartsWithIgnoreCase(name))
                .thenReturn(Arrays.asList(user1, user2));
        when(userMapper.toUserMinimalResponseDto(user1)).thenReturn(responseDto1);
        when(userMapper.toUserMinimalResponseDto(user2)).thenReturn(responseDto2);

        List<UserMinimalResponseDto> result = userService.findByNameLike(name);

        assertEquals(2, result.size());
        assertTrue(result.contains(responseDto1));
        assertTrue(result.contains(responseDto2));
    }

}
