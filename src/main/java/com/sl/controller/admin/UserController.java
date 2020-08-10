package com.sl.controller.admin;

import com.sl.common.exception.GlobalException;
import com.sl.common.i18n.LocaleMessageSource;
import com.sl.common.out.ResultData;
import com.sl.common.out.ResultType;
import com.sl.entity.LoginUser;
import com.sl.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.sl.constant.UserConstant.USER_ADD_NULL;
import static com.sl.constant.UserConstant.USER_DELETE_NAME_NULL;

@RequestMapping("/pri/user")
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
        }else {
            try {
                userService.addUser(user);
                resultData = ResultData.success();
            } catch (GlobalException e) {
                log.error(e.getMessage(), e);
                resultData = new ResultData(e.getResultCode(), e.getMessage());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                resultData = ResultData.fail();
            }

        }
        return resultData;
    }

    @RequestMapping("deleteUser")
    public ResultData deleteUser(String userName){
        ResultData resultData;
        if(StringUtils.isEmpty(userName)){
            resultData = new ResultData(ResultType.FAIL.getResultCode(), LocaleMessageSource.getMessage(USER_DELETE_NAME_NULL));
        }else {
            try {
                userService.deleteUserByUserName(userName);
                resultData = ResultData.success();
            } catch (GlobalException e) {
                log.error(e.getMessage(), e);
                resultData = new ResultData(e.getResultCode(), e.getMessage());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                resultData = ResultData.fail();
            }
        }
        return resultData;
    }

    @RequestMapping("updateUser")
    public ResultData updateUser(LoginUser user){
        ResultData resultData;
        if(null == user){
            resultData = new ResultData(ResultType.FAIL.getResultCode(), LocaleMessageSource.getMessage(USER_ADD_NULL));
        }else {
            try {
                userService.updateUser(user);
                resultData = ResultData.success();
            } catch (GlobalException e) {
                log.error(e.getMessage(), e);
                resultData = new ResultData(e.getResultCode(), e.getMessage());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                resultData = ResultData.fail();
            }
        }
        return resultData;
    }
}
