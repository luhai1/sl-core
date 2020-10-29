package com.sl.controller;

import com.google.zxing.EncodeHintType;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Image;
import com.sl.common.config.sys.DictionaryUtil;
import com.sl.common.elasticsearch.config.ESIndex;
import com.sl.common.elasticsearch.config.ESQueryModel;
import com.sl.common.elasticsearch.config.ESQueryResult;
import com.sl.common.elasticsearch.util.ElasticsearchUtil;
import com.sl.common.excel.innter.ExcelMate;
import com.sl.common.excel.service.ExcelService;
import com.sl.common.exception.GlobalException;
import com.sl.common.i18n.LocaleMessageSource;
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
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.rest.RestStatus;

import org.elasticsearch.search.aggregations.bucket.terms.Terms;

import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.metrics.Sum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.util.*;
import java.util.stream.Collectors;

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

    @RequestMapping("createESIndex")
    public void createESIndex(){
        try {
            ElasticsearchUtil.createIndex("luhai");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequestMapping("createESIndexBean")
    public void createESIndexBean(){
        try {
            ESIndex esIndex = new ESIndex("index_test");
            String mapping = "{\n" +
                    "        \"properties\":{\n" +
                    "            \"id\":{\n" +
                    "                \"type\":\"text\"\n" +
                    "            },\n" +
                    "            \"name\":{\n" +
                    "                \"type\":\"text\"\n" +
                    "            },\n" +
                    "            \"age\":{\n" +
                    "                \"type\":\"integer\"\n" +
                    "            },\n" +
                    "            \"birthday\":{\n" +
                    "                \"type\":\"date\"\n" +
                    "            },\n" +

                    "            \"areaId\":{\n" +
                    "                \"type\":\"integer\"\n" +
                    "            },\n" +
                    "            \"sex\":{\n" +
                    "                \"type\":\"integer\"\n" +
                    "            }\n" +
                    "        }\n" +
                    "}";
            esIndex.setMappingSource(mapping);
            ElasticsearchUtil.createIndex(esIndex);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequestMapping("deleteESIndex")
    public void deleteESIndex(){
        try {
            ElasticsearchUtil.deleteIndex("ncms");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequestMapping("addIndexData")
    public void addIndexData(){
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("id",21);
        dataMap.put("name","张三");
        dataMap.put("age",23);
        dataMap.put("birthday","2020-10-21");
        dataMap.put("sex","1");
        dataMap.put("areaId","0");
        try {
         ElasticsearchUtil.addData("index_test","21",JsonUtil.toJson(dataMap));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @RequestMapping("batchAddData")
    public void batchAddData(){
        List<Map<String,Object>> dataList = new ArrayList<>();
        Map<String,Object> dataMap;
        for(int i=1;i<20;i++){
            dataMap = new HashMap<>();
            dataMap.put("id",i + "");
            dataMap.put("name","张三"+i);
            dataMap.put("age",20 + i);
            dataMap.put("birthday","2020-10-21");
            dataMap.put("sex",i%2);
            dataMap.put("areaId",i%3);
            dataList.add(dataMap);
        }
        try {
            ElasticsearchUtil.batchAddData("index_test",dataList);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequestMapping("deleteData")
    public void deleteData(){
        try {
             ElasticsearchUtil.deleteData("test_index","19");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @RequestMapping("batchDeleteData")
    public void batchDeleteData(){
        List<String> ids = new ArrayList<>();
        ids.add("18");
        ids.add("17");
        ids.add("16");
        try {
             ElasticsearchUtil.batchDeleteData("test_index",ids);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequestMapping("deleteByQuery")
    public void deleteByQuery(){

        DeleteByQueryRequest request = new DeleteByQueryRequest("test_index");
        request.setQuery(new TermQueryBuilder("age", "29"));
        try {
            ElasticsearchUtil.deleteByQuery(request);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequestMapping("queryES")
    public String queryES(){
        ESQueryModel queryModel = new  ESQueryModel();

        String[] includeFields = {"id","name"};
        String[] excludeFields = {"birthday"};
        queryModel.setFrom(0).setSize(10).setIndexName("index_test");
      //  queryModel.setExcludeFields(excludeFields);
      //  queryModel.addSortQuery(queryModel.new SortQuery("age",SortOrder.ASC));
        // 多组聚合 sum、avg统计
        ESQueryModel.AggregationQuery aggregationQuery2 = queryModel.new AggregationQuery();
        aggregationQuery2
                .pushAggTerm(aggregationQuery2.new AggBean("group_of_area1","areaId"))
                .addAggSum(aggregationQuery2.new AggBean("sum_age1","age"));

        ESQueryModel.AggregationQuery aggregationQuery3 = queryModel.new AggregationQuery();
        aggregationQuery3
                .pushAggTerm(aggregationQuery3.new AggBean("group_of_sex","sex"))
                .pushAggTerm(aggregationQuery3.new AggBean("group_of_area2","areaId"))
                .addAggSum(aggregationQuery3.new AggBean("sum_age2","age"))
                .addAggMax(aggregationQuery3.new AggBean("max_age","age"))
                .addAggMin(aggregationQuery3.new AggBean("min_age","age"))
                .addAggAvg(aggregationQuery3.new AggBean("avg_age","age"))
                .addAggCount(aggregationQuery3.new AggBean("count_age","age"))
                .addCardinalityCount(aggregationQuery3.new AggBean("cardina_age","age"));

        queryModel.addAggregationQuery(aggregationQuery2);

        queryModel.addAggregationQuery(aggregationQuery3);
        try {
            ESQueryResult esQueryResult = ElasticsearchUtil.queryData(queryModel);

            return JsonUtil.toJson(esQueryResult);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
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
