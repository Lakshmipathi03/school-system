package com.example.school.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // 🏠 Redirect root URL ("/") to /adi/home
    @GetMapping("/")
    public String redirectToHome() {
        return "redirect:/adi/home";
    }

    // 🏫 Home dashboard page
    @GetMapping("/adi/home")
    public String homePage() {
        return "home"; // will load home.html
    }
}
