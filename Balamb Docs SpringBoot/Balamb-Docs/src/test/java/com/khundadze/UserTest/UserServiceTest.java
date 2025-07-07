package com.khundadze.UserTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.khundadze.Balamb_Docs.dtos.UserMinimalResponseDto;
import com.khundadze.Balamb_Docs.dtos.UserRequestDto;
import com.khundadze.Balamb_Docs.dtos.UserResponseDto;
import com.khundadze.Balamb_Docs.exceptions.UserNotFoundException;
import com.khundadze.Balamb_Docs.models.GlobalRole;
import com.khundadze.Balamb_Docs.models.User;
import com.khundadze.Balamb_Docs.repositories.IUserRepository;
import com.khundadze.Balamb_Docs.services.UserMapper;
import com.khundadze.Balamb_Docs.services.UserService;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void save() {
        UserRequestDto requestDto = new UserRequestDto("Luka", "password123");
        User user = new User("Luka", "password123", GlobalRole.USER);
        UserResponseDto responseDto = new UserResponseDto(1L, "Luka", GlobalRole.USER);

        when(userMapper.toUser(requestDto)).thenReturn(user);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword123");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toUserResponseDto(user)).thenReturn(responseDto);

        UserResponseDto result = userService.save(requestDto);

        assertEquals(responseDto, result);
    }

    @Test
    public void findById_notFound() {
        Long id = 1L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> userService.findById(id));

        assertEquals("User not found with id: " + id, ex.getMessage());
    }

    @Test
    public void findByUsername() {
        String name = "Luka";
        User user = new User("Luka", "password123", GlobalRole.USER);
        UserResponseDto responseDto = new UserResponseDto(1L, "Luka", GlobalRole.USER);

        when(userRepository.findByUsername(name)).thenReturn(Optional.of(user));
        when(userMapper.toUserResponseDto(user)).thenReturn(responseDto);

        UserResponseDto result = userService.findByUsername(name);
        assertEquals(result, responseDto);
    }

    @Test
    public void findByUsername_notFound() {
        String name = "Luka";

        when(userRepository.findByUsername(name)).thenReturn(Optional.empty());

        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> userService.findByUsername(name));

        assertEquals("User not found with name: " + name, ex.getMessage());
    }

    @Test
    public void findByUsernameLike() {
        String name = "Luka";
        User user1 = new User("Luka", "password123", GlobalRole.USER);
        User user2 = new User("Luka_2", "password123", GlobalRole.USER);

        UserMinimalResponseDto responseDto1 = new UserMinimalResponseDto(1L, "Luka");
        UserMinimalResponseDto responseDto2 = new UserMinimalResponseDto(2L, "Luka_2");

        when(userRepository.findTop5ByUsernameStartsWithIgnoreCase(name))
                .thenReturn(Arrays.asList(user1, user2));
        when(userMapper.toUserMinimalResponseDto(user1)).thenReturn(responseDto1);
        when(userMapper.toUserMinimalResponseDto(user2)).thenReturn(responseDto2);

        List<UserMinimalResponseDto> result = userService.findByUsernameLike(name);

        assertEquals(2, result.size());
        assertTrue(result.contains(responseDto1));
        assertTrue(result.contains(responseDto2));
    }

}
