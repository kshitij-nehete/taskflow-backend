package com.taskflow.api.service;

import com.taskflow.api.dto.AuthResponse;
import com.taskflow.api.dto.LoginRequest;
import com.taskflow.api.dto.SignupRequest;
import com.taskflow.api.model.User;
import com.taskflow.api.model.UserRole;
import com.taskflow.api.repository.UserRepository;
import com.taskflow.api.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service                    // This class contains buisness logic
@RequiredArgsConstructor    // Lombok: auto-creats constructor withh all final fields
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private  final PasswordEncoder passwordEncoder;


    public AuthResponse register(SignupRequest request) {

        // Step 1: Check if email is already taken
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // Step 2: Create the user object
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRole(UserRole.USER);

        // Step 3: Save to MongoDB
        User savedUser = userRepository.save(user);

        // Step 4: Generate a JWT token for the new user
        String token = jwtUtil.generateToken(
                savedUser.getEmail(),
                savedUser.getId(),
                savedUser.getRole().name()
        );

        // Step 5: Build and return the response
        return new AuthResponse(
                token,
                new AuthResponse.UserInfo(
                        savedUser.getId(),
                        savedUser.getName(),
                        savedUser.getEmail(),
                        savedUser.getRole().name()
                )
        );
    }

    public AuthResponse login(LoginRequest request) {

        // Step 1: Find the user by email
        User user = userRepository.findByEmail(request.getEmail()).
                orElseThrow(() -> new RuntimeException("Invalid email or password"));


        // Step 2: Check the password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // Step 3: Generate JWT token
        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getId(),
                user.getRole().name()
        );

        // Step 4: Return token + user info
        return new AuthResponse(
                token,
                new AuthResponse.UserInfo(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole().name()
                )
        );
    }
}
