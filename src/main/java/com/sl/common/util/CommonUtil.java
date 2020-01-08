package com.sl.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {
    private CommonUtil(){}
    // 邮箱正则
    private static String EMAIL_PATTERN = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
    // 密码正则(至少8-16个字符，至少1个大写字母，1个小写字母和1个数字)
    private static String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[\\s\\S]{8,16}$";

    public static boolean isEmail(String email){
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher mat = pattern.matcher(email);
        return mat.matches();
    }

    public  static boolean validPassword(String password){
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher mat = pattern.matcher(password);
        return mat.matches();
    }

    public static void main(String[] args){
       boolean email = isEmail("1219695198@qq.com");
       System.out.println(email);
        boolean password = validPassword("10290634");
        System.out.println(password);
        boolean password1 = validPassword("lh10290634");
        System.out.println(password1);
        boolean password2 = validPassword("Lh1029098hgwfwerrrr3r3");
        System.out.println(password2);
    }

}
