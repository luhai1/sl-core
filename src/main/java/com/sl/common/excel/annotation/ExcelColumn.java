package com.sl.common.excel.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelColumn {
    String name() default "";// 列名称

    int index() default 0;// 列在文件中的序号(默认是字段在class中的定义顺序)

    int width() default 0;// 列宽

    String format() default "";// 格式（针对数字和日期生效）

    short height() default 0;// 高度

    int align() default 0;// 布局 1：居左 ，2：居中，3：居右 默认居中

    String fontName() default "";// 字体名称

    short fontSize() default 0;// 字体大小

    boolean bold() default false;// 文字是否加粗 默认true

    String exEl() default "";//导出数据时执行的 spring EL表达式

    String imEl() default "";//导入数据时执行的 spring EL表达式
}
