package com.sl.common.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import lombok.Data;

@Data
public class WaterMark {
    // 透明度
    private float fillOpacity =0.5F;
    //  设置水印颜色
    private BaseColor colorFill = BaseColor.BLACK;
    // 字体
    private BaseFont basefont ;
    // 文字大小
    private float  size =40;
    // 水印对齐方式
    private Integer alignment = Element.ALIGN_RIGHT;
    // 水印内容
    private String waterText;
    // X坐标
    private float waterX=0;
    // Y坐标
    private float waterY=0;
    // 旋转角度
    private float rotation = 45;
}
