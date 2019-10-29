package com.sl.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/pri")
@RestController
public class UserController {
    @RequestMapping("/addUser")
    public String addUser(){
        return "add success";
    }
}
