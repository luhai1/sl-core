package com.sl.controller;

import com.sl.common.exception.GlobalException;
import com.sl.common.i18n.LocaleMessageSource;
import com.sl.common.out.ResultData;
import com.sl.common.out.ResultType;
import com.sl.entity.LoginUser;
import com.sl.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.sl.constant.UserConstant.USER_ADD_NULL;

@RequestMapping("/pri")
@RestController
@Slf4j
public class UserController {
    @Resource
    UserService userService;

    /**
     * 增加用户
     * @param user
     * @return
     */
    @RequestMapping("/addUser")
    public ResultData addUser(LoginUser user){
        ResultData resultData;
        if(null != user){
            resultData = new ResultData(ResultType.FAIL.getResultCode(), LocaleMessageSource.getMessage(USER_ADD_NULL));
        }
        try{
            userService.addUser(user);
        }catch (GlobalException e){
            log.error(e.getMessage(),e);
            resultData = new ResultData(e.getResultCode(),e.getMessage());
        }catch (Exception e){
            log.error(e.getMessage(),e);
            resultData = ResultData.fail();
        }
        resultData = ResultData.success();
        return resultData;
    }


}
