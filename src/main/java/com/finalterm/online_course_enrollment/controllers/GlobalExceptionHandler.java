package com.finalterm.online_course_enrollment.controllers;

import java.security.Principal;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleBadRequest(IllegalArgumentException ex, Model model, Principal principal) {
        model.addAttribute("errorMessage", ex.getMessage());
        if (principal != null) {
            model.addAttribute("username", principal.getName());
        }
        return "user/dashboard";
    }

    @RequestMapping("/error")
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleError(Model model) {
        model.addAttribute("errorMessage", "Đã xảy ra lỗi. Vui lòng thử lại.");
        return "error";
    }
}
