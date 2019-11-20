package test;

import com.sl.entity.SysResources;
import io.netty.handler.codec.base64.Base64Encoder;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.util.Base64;

public class TestMain {
    public static void main(String[] aargs){
        String key = "{\"id\":1,\"status\":0,\"createTime\":null,\"updateTime\":null,\"createId\":null,\"updateId\":null,\"userName\":\"luhai\",\"password\":\"123456\",\"realName\":\"鲁海\",\"idCard\":\"34088119921029063x\",\"sex\":1,\"mobilePhone\":\"18155143649\",\"email\":null,\"roleList\":[{\"id\":2,\"status\":0,\"createTime\":null,\"updateTime\":null,\"createId\":null,\"updateId\":null,\"roleCode\":\"base_user\",\"roleName\":\"基础用户\",\"resourcesList\":[{\"id\":3,\"status\":0,\"createTime\":null,\"updateTime\":null,\"createId\":null,\"updateId\":null,\"resourceCode\":\"dictionary_get\",\"display\":\"字典查询\",\"description\":\"字典查询\",\"parentCode\":\"sys_management\",\"resourceType\":2,\"url\":\"/dic/getDictionary\",\"icon\":null,\"sortBy\":20001}]},{\"id\":1,\"status\":0,\"createTime\":null,\"updateTime\":null,\"createId\":null,\"updateId\":null,\"roleCode\":\"sys_admin\",\"roleName\":\"系统管理员\",\"resourcesList\":[{\"id\":1,\"status\":0,\"createTime\":null,\"updateTime\":null,\"createId\":null,\"updateId\":null,\"resourceCode\":\"user_add\",\"display\":\"增加用户\",\"description\":\"增加用户\",\"parentCode\":\"user_management\",\"resourceType\":3,\"url\":\"/user/addUser\",\"icon\":null,\"sortBy\":10001},{\"id\":2,\"status\":0,\"createTime\":null,\"updateTime\":null,\"createId\":null,\"updateId\":null,\"resourceCode\":\"user_update\",\"display\":\"更改用户\",\"description\":\"更改用户\",\"parentCode\":\"user_management\",\"resourceType\":3,\"url\":\"/user/updateUser\",\"icon\":null,\"sortBy\":10002}]}]}";
        byte[] bytes = key.getBytes();
        BASE64Encoder encoder = new BASE64Encoder();
        BASE64Decoder decoder = new BASE64Decoder();
        System.out.println(new String(bytes));
    }
}
