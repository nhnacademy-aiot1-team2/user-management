package com.user.management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
    @GetMapping("/loginForm")
    public String login() {
        return "loginForm";
    }

    @GetMapping("/")
    public String home()
    {
        return "index";
    }
}