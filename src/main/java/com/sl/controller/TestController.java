package com.sl.controller;

import com.sl.common.i18n.LocaleMessageSource;
import com.sl.service.DictionaryService;
import com.sl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/pub/test")
public class TestController {

    @Resource
    UserService userService;
    @Resource
    DictionaryService dictionaryService;

    @RequestMapping("getResult")
    public String getResult(){
        String value = LocaleMessageSource.getMessage("slcore.add.success");
      return value;
    }
}
