package com.rk.bookgallery.service;

import com.rk.bookgallery.entity.User;
import com.rk.bookgallery.exception.ResourceNotFoundException;
import com.rk.bookgallery.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
