package com.mantra.auth.services;

import com.mantra.auth.dto.AuthResponse;
import com.mantra.auth.dto.LoginRequest;
import com.mantra.auth.dto.RegisterRequest;
import com.mantra.auth.dto.UserDTO;
import com.mantra.auth.entities.User;
import com.mantra.auth.repositories.UserRepository;
import com.mantra.shared.enums.Role;
import com.mantra.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);
        String jwt = jwtUtils.generateToken(savedUser);

        return new AuthResponse(jwt, savedUser.getId(), savedUser.getEmail(), 
                               savedUser.getFirstName(), savedUser.getLastName(), savedUser.getRole());
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        String jwt = jwtUtils.generateToken(user);

        return new AuthResponse(jwt, user.getId(), user.getEmail(), 
                               user.getFirstName(), user.getLastName(), user.getRole());
    }

    public UserDTO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        
        return new UserDTO(user.getId(), user.getEmail(), user.getFirstName(), 
                          user.getLastName(), user.getRole(), user.getCreatedAt(), user.getUpdatedAt());
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserDTO(user.getId(), user.getEmail(), user.getFirstName(),
                                       user.getLastName(), user.getRole(), user.getCreatedAt(), user.getUpdatedAt()))
                .collect(Collectors.toList());
    }
}