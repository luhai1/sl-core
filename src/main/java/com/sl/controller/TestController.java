package com.sl.controller;

import com.sl.service.DictionaryService;
import com.sl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    UserService userService;
    @Resource
    DictionaryService dictionaryService;

    @RequestMapping("getResult")
    public String getResult(){
      return   userService.getEmailByUserName("lh") + dictionaryService.getNameByCode("lh");
    }
}
