package com.sl.common.excel.innter;

import java.util.List;
import java.util.Set;

public class ExcelMate {
    private String tpl;
    private Class<?> clazz;
    private Set<Class<?>> superclazz;
    private String name;
    private int height;
    private int fontSize;
    private List<ExcelRowMate> rows;
    private List<Column> headers;
    private int headerAlign;
    private String headerFontName;
    private short headerFontSize;
    private short headerHeight;
    private boolean headerBold;
    private boolean hssf;
    private byte[] data;

    public String getName() { return this.name; }



    public void setName(String name) { this.name = name; }



    public int getHeight() { return this.height; }



    public void setHeight(int height) { this.height = height; }



    public int getFontSize() { return this.fontSize; }



    public void setFontSize(int fontSize) { this.fontSize = fontSize; }



    public List<Column> getHeaders() { return this.headers; }



    public void setHeaders(List<Column> headers) { this.headers = headers; }



    public Class<?> getClazz() { return this.clazz; }



    public void setClazz(Class<?> clazz) { this.clazz = clazz; }



    public Set<Class<?>> getSuperclazz() { return this.superclazz; }



    public void setSuperclazz(Set<Class<?>> superclazz) { this.superclazz = superclazz; }



    public List<ExcelRowMate> getRows() { return this.rows; }



    public void setRows(List<ExcelRowMate> rows) { this.rows = rows; }



    public int getHeaderAlign() { return this.headerAlign; }



    public void setHeaderAlign(int headerAlign) { this.headerAlign = headerAlign; }



    public String getHeaderFontName() { return this.headerFontName; }



    public void setHeaderFontName(String headerFontName) { this.headerFontName = headerFontName; }



    public boolean isHeaderBold() { return this.headerBold; }



    public void setHeaderBold(boolean headerBold) { this.headerBold = headerBold; }



    public void setHeaderFontSize(short headerFontSize) { this.headerFontSize = headerFontSize; }



    public void setHeaderHeight(short headerHeight) { this.headerHeight = headerHeight; }



    public short getHeaderFontSize() { return this.headerFontSize; }



    public short getHeaderHeight() { return this.headerHeight; }



    public String getTpl() { return this.tpl; }



    public void setTpl(String tpl) { this.tpl = tpl; }



    public boolean isHssf() { return this.hssf; }



    public void setHssf(boolean hssf) { this.hssf = hssf; }



    public byte[] getData() { return this.data; }



    public void setData(byte[] data) { this.data = data; }


    public String getFullName() {
        if (isHssf()) {
            return String.valueOf(getName()) + ".xls";
        }
        return String.valueOf(getName()) + ".xlsx";
    }
}
