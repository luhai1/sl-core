package com.sl.common.pdf;

import java.io.File;

public interface PdfConstant {
    // pdf临时文件路径
    String PDF_TEMP_PATH = "D:/home/data/app" + File.separator+"pdf";
    // pdf模板数据不能为空
    String PDF_FTL_DATA_EMPTY="pdf_ftl_data_empty";
    // pdf中文字体路径
    String PDF_ARIALUNI_PATH="/templates/ftl/fonts/arialuni.ttf";
    String PDF_SIMSUN_PATH="/templates/ftl/fonts/simsun.ttc";
}
