package com.sl.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Image;
import com.sl.common.config.sys.DictionaryUtil;
import com.sl.common.excel.innter.ExcelMate;
import com.sl.common.excel.service.ExcelService;
import com.sl.common.exception.GlobalException;
import com.sl.common.i18n.LocaleMessageSource;
import com.sl.common.out.ResultData;
import com.sl.common.out.ResultType;
import com.sl.common.pdf.*;
import com.sl.common.qrCode.QrCodeUtil;
import com.sl.common.util.JsonUtil;
import com.sl.entity.LoginUser;
import com.sl.feign.TestFeign;
import com.sl.service.DictionaryService;
import com.sl.service.UserService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @RequestMapping("testPdf")
    public void testPdf( HttpServletResponse response) {
        PdfData pdfData = getPdfData();
        PdfUtil.generateToPdf(pdfData,response);
    }

    @RequestMapping("testQrcode")
    public void testQrcode( HttpServletResponse response) {
        Map<EncodeHintType, String> hints = new HashMap();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        try{
            QrCodeUtil.createQrCode("http://www.baidu.com",400,400,"JPG",response.getOutputStream());
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private PdfData getPdfData(){
        PdfData pdfData = new PdfData();
        List<PdfFtlData> pdfFtlDataList = new ArrayList<>();
        PdfFtlData pdfFtlData = new PdfFtlData();
//        pdfData.setNeedWaterImage(true);
        WaterImage waterImage = new WaterImage();
        waterImage.setImagePath("/templates/ftl/bijiegongzhang.jpg");
        waterImage.setScaleAbsoluteWidth(100);
        waterImage.setScaleAbsoluteHeight(100);
        waterImage.setFillOpacity(1);
        waterImage.setRotation(90);
        waterImage.setRotationDegrees(90);
        pdfData.setWaterImage(waterImage);

        WaterMark waterMark = new WaterMark();
        waterMark.setWaterX(250);
        waterMark.setWaterY(200);
        waterMark.setWaterText("水印文字");
        waterMark.setSize(50);
        waterMark.setColorFill(BaseColor.GRAY);
        waterMark.setFillOpacity(1);
        pdfFtlData.setTemplateFileName("unitPayPlan.ftl");
        pdfFtlData.setTemplatePath("templates/ftl");
        Map<String,Object> tmpData = new HashMap();
        Map<String,Object> data = new HashMap();

        List<Map<String,Object>> unitPayPlanList = new ArrayList();
        for(int i = 0;i<3;i++){
            Map<String,Object> obj = new HashMap();
            obj.put("insuredType","职工"+i);
            obj.put("payState","已支付");
            obj.put("insuredHandleOrg","insuredHandleOrg"+i);
            obj.put("insuredNum","100"+i);
            obj.put("startTime","20200601"+i);
            obj.put("endTime","20200624"+i);
            unitPayPlanList.add(obj);
        }
        data.put("unitName","晶奇");
        data.put("creditCode","123456");
        data.put("unitCode","115211");
        data.put("unitPayPlanList",unitPayPlanList);

        data.put("currentDate","2020-06-24");
        tmpData.put("data",data);

        pdfFtlData.setTemplateData(tmpData);
        pdfFtlData.setNeedWaterImage(true);
        pdfFtlData.setWaterImage(waterImage);
        pdfFtlData.setNeedWaterMark(true);
        pdfFtlData.setWaterMark(waterMark);
        pdfFtlDataList.add(pdfFtlData);
        PdfFtlData pdfFtlData1 = new PdfFtlData();
        pdfFtlData1.setTemplateFileName("test.ftl");
        pdfFtlData1.setTemplatePath("templates/ftl");
        Map<String,Object> tmpData1 = new HashMap();
        Map<String,Object> data1 = new HashMap();
        data1.put("testData","luhaipt");
        tmpData1.put("data",data1);
        pdfFtlData1.setTemplateData(tmpData1);
        pdfFtlData1.setNeedWaterMark(true);
        pdfFtlData1.setWaterMark(waterMark);
      //  pdfFtlDataList.add(pdfFtlData1);
       // pdfFtlDataList.add(pdfFtlData);
       // pdfFtlDataList.add(pdfFtlData1);
       // pdfFtlDataList.add(pdfFtlData);
        pdfData.setPdfFtlDataList(pdfFtlDataList);

        PdfHeader pdfHeader = new PdfHeader();
        pdfHeader.setHeaderText("这是页眉");
        pdfHeader.setAlignment(0);
        pdfHeader.setRightSpace(250);
        pdfHeader.setTopSpace(-20);

        PdfFooter pdfFooter = new PdfFooter();
        pdfFooter.setHeaderText("-");
        pdfFooter.setAlignment(2);
        pdfFooter.setLeftSpace(260);
        pdfFooter.setBottomSpace(-10);

        pdfData.setNeedHeader(true);
        pdfData.setNeedFooter(true);
        pdfData.setPdfHeader(pdfHeader);
        pdfData.setPdfFooter(pdfFooter);
        // todo 目录
        try{
            Image image =  Image.getInstance(IOUtils.toByteArray(new ClassPathResource("/templates/ftl/115121.jpg").getInputStream()));
            pdfHeader.setImage(image);
            pdfFooter.setImage(image);
        }catch (Exception e){
            e.printStackTrace();
        }

       // pdfData.setNeedCatalog(true);
        PdfCatalog pdfCatalog = new PdfCatalog();
        Map<String, Integer> catalogs = new HashMap<>();
        catalogs.put("表格1",1);
        catalogs.put("test1",4);
        catalogs.put("表格2",5);
        catalogs.put("test2",8);
        catalogs.put("表格3",9);
      //  pdfCatalog.setCatalogs(catalogs);
       // pdfData.setPdfCatalog(pdfCatalog);
        return pdfData;
    }
}
