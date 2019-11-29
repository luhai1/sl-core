package com.sl.common.out;


import com.sl.common.i18n.LocaleMessageSource;

import static com.sl.common.out.MessageProperties.*;

public enum ResultType {
    SUCCESS("200", LocaleMessageSource.getMessage(RESULT_TYPE_SUCCESS)),
    FAIL("401",LocaleMessageSource.getMessage(RESULT_TYPE_FAIL)),
    SYS_EXCEPTION("400",LocaleMessageSource.getMessage(RESULT_TYPE_SYS_EXCEPTION)),
    UNLOGIN("201",LocaleMessageSource.getMessage(RESULT_TYPE_UN_LOGIN));
    private String resultCode;
    private String message;

    private ResultType(String resultCode,String message){
        this.resultCode = resultCode;
        this.message = message;
    }
    public String getResultCode(){
        return this.resultCode;
    }

    public void setResultCode(String resultCode){
        this.resultCode = resultCode;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public  String getMessage(){
        return this.message;
    }
}
