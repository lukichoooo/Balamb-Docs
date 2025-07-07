package com.khundadze.Balamb_Docs.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.khundadze.Balamb_Docs.configuration.JwtService;
import com.khundadze.Balamb_Docs.exceptions.UserNotFoundException;
import com.khundadze.Balamb_Docs.models.GlobalRole;
import com.khundadze.Balamb_Docs.models.User;
import com.khundadze.Balamb_Docs.repositories.IUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .globalRole(GlobalRole.USER)
                .build();
        userRepository.save(user);
        var JwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(JwtToken).build();
    }

    public AuthenticationResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()));
        } catch (Exception e) {
            // You can throw a custom exception or return a specific response
            throw new RuntimeException("Invalid username or password");
        }

        var userOptional = userRepository.findByUsername(request.getUsername());
        if (userOptional.isEmpty())
            throw new UserNotFoundException("User not found with username: " + request.getUsername());
        var user = userOptional.get();
        var JwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(JwtToken).build();
    }

}
