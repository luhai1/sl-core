package com.sl.common.pdf;

import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import lombok.Data;

import java.awt.*;


/**
 * 页眉
 */
@Data
public class PdfHeader {
    // 字体
    BaseFont font;
    // 大小
    private float size =10;
    // 格式（加粗、斜体等）
    private int style = Font.NORMAL;
    // //设置是否有边框等
    private int border = Rectangle.BOTTOM;
    /**
     * 0是靠左
     * 1是居中
     * 2是居右
     */
    private  int alignment = 1;
    // 页眉的左右间距
    private  float rightSpace = 0;
    // 页眉的上下间距
    private  float topSpace = -20;
    // 旋转角度
    private float rotation = 0;
    // 颜色
    private Color borderColor = Color.black;
    // 页眉文字
    private String headerText="";
    // 页眉图片
    private Image image;
    private float imageX = 0;
    private float imageY = -30;
}
