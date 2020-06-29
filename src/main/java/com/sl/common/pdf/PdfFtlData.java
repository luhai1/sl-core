package com.sl.common.pdf;

import lombok.Data;

import java.util.Map;

@Data
public class PdfFtlData {
    // 是否需要加图片水印
    private  boolean needWaterImage = false;
    // 当前模板对应的图片水印
    private WaterImage waterImage;
    // 是否要加文字水印
    private  boolean needWaterMark = false;
    // 当前模板对应的图片水印
    private WaterMark waterMark;
    // 模板文件路径不包括文件
    private  String templatePath;
    //  模板文件名
    private  String templateFileName;
    // 模板文件对应数据
    Map<String,Object> templateData;

}
