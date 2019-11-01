package com.sl.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/pri")
@RestController
public class UserController {
    @RequestMapping("/addUser")
    public String addUser(HttpServletRequest request){
        return "add success";
    }
}
