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
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .globalRole(GlobalRole.USER)
                .build();
        userRepository.save(user);
        var JwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(JwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail());
        if (user == null)
            throw new UserNotFoundException("User not found with email: " + request.getEmail());
        var JwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(JwtToken).build();
    }
}
