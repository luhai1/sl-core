package com.sl.controller;

import com.sl.common.config.sys.DictionaryUtil;
import com.sl.common.excel.innter.ExcelMate;
import com.sl.common.excel.service.ExcelService;
import com.sl.common.exception.GlobalException;
import com.sl.common.i18n.LocaleMessageSource;
import com.sl.common.out.ResultData;
import com.sl.common.out.ResultType;
import com.sl.common.util.JsonUtil;
import com.sl.entity.LoginUser;
import com.sl.feign.TestFeign;
import com.sl.service.DictionaryService;
import com.sl.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.FileOutputStream;
import java.util.List;

@RestController
@RefreshScope
@RequestMapping("/pub/test")
public class TestController {

    @Resource
    UserService userService;
    @Resource
    DictionaryService dictionaryService;
    @Autowired
    ExcelService excelService;
    @Resource
    TestFeign testFeign;

    @Value("${user.name}")
    String userName;
    @RequestMapping("getI18n")
    public String getI18n(){
        String value = LocaleMessageSource.getMessage("slcore.add.success");
        return value;
    }

    @RequestMapping("getDict")
    public String getDict(String itemCode){
        String value = DictionaryUtil.getDictItemValue(itemCode);
        String value1 = JsonUtil.toJson(DictionaryUtil.getDictValue(itemCode));
        return value+value1;
    }

    @RequestMapping("testException")
    public void testException(String testName){
        if(StringUtils.isBlank(testName)){
            throw  new RuntimeException("testName 不能为空");
        }
        if("testName".equals(testName)){
            throw  new GlobalException(ResultType.FAIL.getResultCode(),"testName参数错误");
        }
        testName = testName.substring(0,6);

    }

    @RequestMapping("testExport")
    public void testExport() {
        List<LoginUser> list = userService.selectAll();
        ExcelMate excelMate = excelService.export(list);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("D:\\" + excelMate.getFullName());
            fileOutputStream.write(excelMate.getData());
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("testNacosConfig")
    public String testNacosConfig() {
        return userName;
    }


    @RequestMapping("testNacosFeign")
    public String testNacosFeign() {
        return testFeign.testFeign();
    }
}
