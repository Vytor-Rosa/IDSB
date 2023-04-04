package net.weg.gedesti.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gedesti")
public class FrontController {
    @GetMapping("/login")
    public String login(){
        return "login";
    }
}
