package com.sl.common.pdf;

import lombok.Data;

@Data
public class WaterImage {
    private String imagePath;
    // 位置横坐标
    private float absoluteX=0;
    // 位置横坐标
    private float absoluteY=0;
    // 图片宽
    private float scaleAbsoluteWidth=0;
    // 图片高
    private float scaleAbsoluteHeight=0;
    // 设置旋转弧度
    private float rotation=0;
    // 设置旋转角度
    private float rotationDegrees=0;
    // 设置等比缩放
    private float  scalePercent =100;
    // 透明度
    private float fillOpacity =0.5F;
}
