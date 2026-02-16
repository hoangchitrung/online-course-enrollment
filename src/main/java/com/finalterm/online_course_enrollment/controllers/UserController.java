package com.finalterm.online_course_enrollment.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @GetMapping("/")
    public String homePage() {
        return "index";
    }
}
