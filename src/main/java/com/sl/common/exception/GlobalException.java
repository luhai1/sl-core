package com.sl.common.exception;

import lombok.Data;

@Data
public class GlobalException extends RuntimeException {

    private String resultCode;

    public GlobalException(String msg){
        super(msg);
    }
    public  GlobalException(String resultCode, String msg){
        super(msg);
        this.resultCode = resultCode;
    }






}
