package com.rk.bookgallery.service;

import com.rk.bookgallery.dto.AuthResponse;
import com.rk.bookgallery.dto.LoginRequest;
import com.rk.bookgallery.dto.RegisterRequest;
import com.rk.bookgallery.dto.UserResponse;
import com.rk.bookgallery.entity.User;
import com.rk.bookgallery.entity.UserRole;
import com.rk.bookgallery.exception.BadRequestException;
import com.rk.bookgallery.repository.UserRepository;
import com.rk.bookgallery.security.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail().trim())) {
            throw new BadRequestException("User already exists with this email");
        }

        User user = new User();
        user.setName(request.getName().trim());
        user.setEmail(request.getEmail().trim());
        user.setPhone(request.getPhone().trim());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.ROLE_USER);

        User savedUser = userRepository.save(user);
        String token = jwtUtil.generateToken(savedUser.getEmail());
        return new AuthResponse(token, new UserResponse(savedUser));
    }

    public AuthResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            if (!authentication.isAuthenticated()) {
                throw new BadRequestException("Invalid email or password");
            }

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new BadRequestException("User not found"));

            String token = jwtUtil.generateToken(user.getEmail());
            return new AuthResponse(token, new UserResponse(user));
        } catch (BadCredentialsException ex) {
            throw new BadRequestException("Invalid email or password");
        }
    }
}
