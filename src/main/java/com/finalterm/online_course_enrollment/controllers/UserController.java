package com.finalterm.online_course_enrollment.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    @GetMapping("/")
    public String homePage() {
        return "user/index";
    }
}
