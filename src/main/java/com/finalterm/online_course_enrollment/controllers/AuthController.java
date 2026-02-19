package com.finalterm.online_course_enrollment.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finalterm.online_course_enrollment.controllers.dto.RegisterRequest;
import com.finalterm.online_course_enrollment.models.User;
import com.finalterm.online_course_enrollment.services.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping
@Validated
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            User saved = authService.register(request);

            // no return password/hash to outside
            RegisterResponse response = new RegisterResponse(saved.getId(), saved.getEmail(), saved.getFullName(),
                    saved.getUserType().name());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException ex) {
            // TODO: handle exception
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }
    }

    public record RegisterResponse(Long id, String email, String fullName, String userType) {
    }
}
