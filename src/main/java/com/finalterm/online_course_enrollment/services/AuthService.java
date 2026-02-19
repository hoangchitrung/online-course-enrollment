package com.finalterm.online_course_enrollment.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.finalterm.online_course_enrollment.controllers.dto.RegisterRequest;
import com.finalterm.online_course_enrollment.models.User;
import com.finalterm.online_course_enrollment.models.enums.UserType;
import com.finalterm.online_course_enrollment.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register(RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("Email already exists: " + request.email());

        }
        User user = new User();
        user.setEmail(request.email());
        user.setFullName(request.fullName());
        user.setPassword((passwordEncoder.encode(request.password()))); // Brcrypt here
        user.setUserType(UserType.USER);

        return userRepository.save(user);
    }
}
