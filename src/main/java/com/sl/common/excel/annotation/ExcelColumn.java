package com.sl.common.excel.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelColumn {
    String name() default "";

    int index() default 0;

    int width() default 0;

    String format() default "";

    short height() default 0;

    int align() default 0;

    String fontName() default "";

    short fontSize() default 0;

    boolean bold() default false;

    String exEl() default "";

    String imEl() default "";
}
