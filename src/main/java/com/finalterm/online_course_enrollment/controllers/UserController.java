package com.finalterm.online_course_enrollment.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class UserController {
    @GetMapping("/")
    public String homePage() {
        return "user/index";
    }
    
}
