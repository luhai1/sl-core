package com.sl.controller;

import com.sl.common.out.ResultData;
import com.sl.entity.LoginUser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/pri")
@RestController
public class UserController {
    @RequestMapping("/addUser")
    public ResultData addUser(LoginUser user){


    }


}
