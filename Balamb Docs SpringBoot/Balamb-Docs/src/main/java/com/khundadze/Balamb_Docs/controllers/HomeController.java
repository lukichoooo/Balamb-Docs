package com.khundadze.Balamb_Docs.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/home")
public class HomeController {

    @GetMapping("/welcomeText")
    public String home() {
        return "Welcome to Balamb Docs — your place to create, collaborate, and share documents securely!";
    }
}
