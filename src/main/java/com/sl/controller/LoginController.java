package com.sl.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("")
@RestController
public class LoginController {

    @RequestMapping("doLogin")
    public String doLogin(String username,String password){
        return  "success";
    }
}
