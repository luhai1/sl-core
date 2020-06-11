package com.sl.common.excel.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Excel {
    String name() default ""; // Excel文件名称

    int height() default 0;// 行高（全局）

    short fontSize() default 0;// 字体大小（全局）

    String tpl() default "";// 模板

    short headerHeight() default 0;// 表头高度

    int headerAlign() default 2;// 表头文字布局 1：居左 ，2：居中，3：居右 默认居中

    String headerFontName() default "";// 表头文字字体名称

    short headerFontSize() default 0;// 表头文字字体大小

    boolean headerBold() default true;// 表头文字加粗 默认true
}
