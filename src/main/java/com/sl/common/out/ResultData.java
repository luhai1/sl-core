package com.sl.common.out;

import lombok.Data;

@Data
public class ResultData {
    private String resultCode;
    private String resultMeg;
    private Object data;

    public  ResultData(){}
    public  ResultData(String resultCode,String resultMesg){
        this.resultCode = resultCode;
        this.resultMeg =resultMesg;
    }
    public  ResultData(String resultCode,String resultMesg,Object data){
        this.resultCode = resultCode;
        this.resultMeg = resultMesg;
        this.data = data;
    }

    public ResultData(ResultType resultType){
        this.resultCode = resultType.getResultCode();
        this.resultMeg =resultType.getMessage();
    }

    public static ResultData success(){
        return new ResultData(ResultType.SUCCESS);
    }

    public static ResultData fail(){
        return new ResultData(ResultType.SYS_EXCEPTION);
    }
}
