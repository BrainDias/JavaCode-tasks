package org.security.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class HomeController {

    @GetMapping("/home")
    public String home() {
        return "Welcome to the public home page!";
    }

    @GetMapping("/private")
    public String privatePage() {
        return "This is a protected page. You must be logged in!";
    }
}

