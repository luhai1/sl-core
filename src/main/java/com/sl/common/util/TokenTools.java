package com.sl.common.util;

import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Token的工具类
 */
public class TokenTools {

         /**
          * 生成Token
          * @return
          */

    public static String makeToken(Long timeStamp,String key) {
        String token = timeStamp + key;

        try {

            MessageDigest md = MessageDigest.getInstance("md5");

            byte md5[] = md.digest(token.getBytes());

            BASE64Encoder encoder = new BASE64Encoder();

            return encoder.encode(md5);

        } catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }


}


