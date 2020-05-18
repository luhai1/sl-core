package com.sl.common.excel.innter;

import com.alibaba.fastjson.JSON;
import lombok.Data;

@Data
public class Column
{
    private String columnId;
    private String columnName;
    private String fieldName;
    private Class<?> fieldType;
    private int index;
    private int width;
    private String format;
    private Object value;
    private short height;
    private int align;
    private String fontName;
    private short fontSize;
    private boolean bold;
    private String exEl;
    private String imEl;

    public int getIndex() { return this.index; }



    public void setIndex(int index) { this.index = index; }



    public int getWidth() { return this.width; }



    public void setWidth(int width) { this.width = width; }



    public String getFormat() { return this.format; }



    public void setFormat(String format) { this.format = format; }



    public Object getValue() { return this.value; }



    public void setValue(Object value) { this.value = value; }



    public String getColumnName() { return this.columnName; }



    public void setColumnName(String columnName) { this.columnName = columnName; }



    public String getFieldName() { return this.fieldName; }



    public void setFieldName(String fieldName) { this.fieldName = fieldName; }



    public short getHeight() { return this.height; }



    public void setHeight(short height) { this.height = height; }



    public int getAlign() { return this.align; }



    public void setAlign(int align) { this.align = align; }



    public String getFontName() { return this.fontName; }



    public void setFontName(String fontName) { this.fontName = fontName; }



    public short getFontSize() { return this.fontSize; }



    public void setFontSize(short fontSize) { this.fontSize = fontSize; }



    public boolean isBold() { return this.bold; }



    public void setBold(boolean bold) { this.bold = bold; }



    public String getColumnId() { return this.columnId; }



    public void setColumnId(String columnId) { this.columnId = columnId; }



    public String getExEl() { return this.exEl; }



    public void setExEl(String exEl) { this.exEl = exEl; }



    public String getImEl() { return this.imEl; }



    public void setImEl(String imEl) { this.imEl = imEl; }



    public Class<?> getFieldType() { return this.fieldType; }



    public void setFieldType(Class<?> fieldType) { this.fieldType = fieldType; }




    public String toString() { return JSON.toJSONString(this); }
}