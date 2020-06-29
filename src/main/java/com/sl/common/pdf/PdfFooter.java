package com.sl.common.pdf;

import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import lombok.Data;

import java.awt.*;


/**
 * 页脚
 */
@Data
public class PdfFooter {
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
    // 页脚的左右间距
    private  float leftSpace = 260;
    // 页脚的上下间距
    private  float bottomSpace = -10;
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
