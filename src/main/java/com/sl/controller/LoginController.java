package com.sl.controller;

import com.sl.common.config.shiro.UserNamePasswordTimeToken;
import com.sl.common.util.JedisUtil;
import com.sl.common.util.JsonUtil;
import com.sl.common.util.TokenTools;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("")
@RestController
public class LoginController {

    @RequestMapping("doLogin")
    public String doLogin(String userName,String password,Long timeStamp){
        Subject subject = SecurityUtils.getSubject();
        String tokenKey = null;
        UserNamePasswordTimeToken token = new UserNamePasswordTimeToken(userName,password);
        try {
            subject.login(token);
            tokenKey = TokenTools.makeToken(timeStamp,userName);
        }  catch (Exception e) {

        }
        return JsonUtil.toJson(JedisUtil.get(tokenKey));
    }
}
