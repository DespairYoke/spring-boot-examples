package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin")
public class AdminController {


    @GetMapping("/greeting")
    public String greeting() {
        return "Hello,World!";
    }

    @GetMapping("/login")
    public String login() {

        return "login sucess";
    }
}
