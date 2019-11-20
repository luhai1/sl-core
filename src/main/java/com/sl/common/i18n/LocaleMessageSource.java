package com.sl.common.i18n;


import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Locale;

@Component
public class LocaleMessageSource {

    private LocaleMessageSource(){}

    @Resource
    private MessageSource autoMessageSource;

    private static MessageSource messageSource;

    @PostConstruct
    public void  initMessageSource(){
        messageSource = autoMessageSource;
    }
    /**
      * @param code ：对应messages配置的key.
      * @return
      */
     public static String getMessage(String code){
        return getMessage(code,null);
     }

    /**
      *
      * @param code ：对应messages配置的key.
      * @param args : 数组参数.
      * @return
      */
     public static String getMessage(String code,Object[] args){
        return getMessage(code, args,"");
     }

     /**
      *
      * @param code ：对应messages配置的key.
      * @param args : 数组参数.
      * @param defaultMessage : 没有设置key的时候的默认值.
      * @return
      */
      public static String getMessage(String code,Object[] args,String defaultMessage){
        //这里使用比较方便的方法，不依赖request.
         Locale locale = LocaleContextHolder.getLocale();
         return messageSource.getMessage(code, args, defaultMessage, locale);
    }

}
