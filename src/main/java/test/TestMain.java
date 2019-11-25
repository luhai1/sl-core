package test;

import com.sl.entity.SysResources;
import io.netty.handler.codec.base64.Base64Encoder;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.util.Base64;

public class TestMain {
    public static void main(String[] aargs){
        String registTime = "2019-10";
        System.out.println(registTime.indexOf("-"));
        System.out.println(registTime.length()-1);
        String m = registTime.substring(registTime.indexOf("-")+1);
        System.out.println(m);
    }
}
