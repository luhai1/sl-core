package com.sl.common.pdf;

import com.itextpdf.layout.property.TextAlignment;
import lombok.Data;

import java.util.Map;

@Data
public class PdfCatalog {
    //目录上方第一行文字
    private String catalogTitle = "目录";
    // 目录数据map<标题,页码>
    private Map<String, Integer> catalogs;
    //目录占页大小(为锚点偏移做参数)
    private int catalogPageSize =1;

}
