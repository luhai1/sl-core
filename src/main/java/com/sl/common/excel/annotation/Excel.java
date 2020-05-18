package com.sl.common.excel.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Excel {
    String name() default "";

    int height() default 0;

    short fontSize() default 0;

    String tpl() default "";

    short headerHeight() default 0;

    int headerAlign() default 2;

    String headerFontName() default "";

    short headerFontSize() default 0;

    boolean headerBold() default true;
}
