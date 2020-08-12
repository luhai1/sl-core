package com.sl.common.exception;

import com.sl.common.i18n.LocaleMessageSource;
import com.sl.common.out.ResultData;
import com.sl.common.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import static com.sl.common.out.MessageProperties.REQUEST_PARAM;
import static com.sl.common.out.MessageProperties.REQUEST_URL;

@ControllerAdvice
@Slf4j
public class GlobalExceptionAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResultData defaultException(HttpServletRequest request, Exception e){
        log.info(LocaleMessageSource.getMessage(REQUEST_URL)+request.getRequestURL());
        log.info(LocaleMessageSource.getMessage(REQUEST_PARAM)+ JsonUtil.toJson(request.getParameterMap()));
        log.error(e.getMessage(),e);
        return ResultData.fail();
    }
    @ExceptionHandler(GlobalException.class)
    @ResponseBody
    public  ResultData globalException(HttpServletRequest request, GlobalException e){
        log.info(LocaleMessageSource.getMessage(REQUEST_URL)+request.getRequestURL());
        log.info(LocaleMessageSource.getMessage(REQUEST_PARAM)+JsonUtil.toJson(request.getParameterMap()));
        log.error(e.getMessage(),e);
        return new ResultData(e.getResultCode(),e.getMessage());
    }
}
