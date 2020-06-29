package com.sl.common.pdf;

import lombok.Data;

import java.util.List;

@Data
public class PdfData {
    // 是否需要加图片水印
    private  boolean needWaterImage = false;
    // 图片水印
    private WaterImage waterImage;
    // 是否要加文字水印
    private  boolean needWaterMark = false;
    // 文字水印
    private WaterMark waterMark;
    // 导出文件名
    private String outPdfName;
    // 多模板数据
    private List<PdfFtlData> pdfFtlDataList;
    // 是否要加页眉
    private  boolean needHeader = false;
    // 是否要加页脚
    private  boolean needFooter = false;
    // 页眉
    private PdfHeader pdfHeader;
    // 页脚
    private PdfFooter pdfFooter;
    // 是否要生成目录
    private boolean needCatalog=false;
    private PdfCatalog pdfCatalog;
}
