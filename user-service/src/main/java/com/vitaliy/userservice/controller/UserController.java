package com.vitaliy.userservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @GetMapping("/me")
    public String getCurrentUser() {
        return "User endpoint works";
    }


    @GetMapping("/admin")
    public String adminOnly() {
        return "Admin endpoint works";
    }
}
