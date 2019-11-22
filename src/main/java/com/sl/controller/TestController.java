package com.sl.controller;

import com.sl.common.config.dict.DictionaryUtil;
import com.sl.common.i18n.LocaleMessageSource;
import com.sl.common.util.JsonUtil;
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

    @RequestMapping("getI18n")
    public String getI18n(){
        String value = LocaleMessageSource.getMessage("slcore.add.success");
        return value;
    }

    @RequestMapping("getDict")
    public String getDict(String itemCode){
        String value = DictionaryUtil.getDictItemValue(itemCode);
        String value1 = JsonUtil.toJson(DictionaryUtil.getDictValue(itemCode));
        return value+value1;
    }
}
