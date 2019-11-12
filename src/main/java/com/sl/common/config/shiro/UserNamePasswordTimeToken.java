package com.sl.common.config.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

import java.io.Serializable;

public class UserNamePasswordTimeToken extends UsernamePasswordToken implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 4812793519945855483L;

    // 手机号码
    private Long timeStamp;




    public UserNamePasswordTimeToken() {
    }

    public UserNamePasswordTimeToken(final Long timeStamp) {
        // TODO Auto-generated constructor stub
        this.timeStamp = timeStamp;
    }

    public UserNamePasswordTimeToken(final String userName, final String password) {
        // TODO Auto-generated constructor stub
        super(userName, password);
    }

    public Long getTimeStamp(Long timeStamp) {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }


}
