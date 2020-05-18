package com.sl.common.excel.innter;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.spel.standard.SpelExpression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.objenesis.Objenesis;
import org.springframework.objenesis.ObjenesisStd;
import org.springframework.objenesis.instantiator.ObjectInstantiator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class WorkbookParser {
    public static final Logger LOGGER = LoggerFactory.getLogger(WorkbookParser.class);
    private static final MateDataParser mateDataParser = new MateDataParser();
    private static final SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat dayTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final SimpleDateFormat dayTimeSecondFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SpelExpressionParser parser = new SpelExpressionParser();
    private static final Objenesis OBJENESIS = new ObjenesisStd();
    private static final ConcurrentHashMap<String, ObjectInstantiator<?>> INSTANTIATORS = new ConcurrentHashMap<>();

    public ExcelMate export(List<?> list) {
        if (CollectionUtils.isEmpty(list)) {
            throw new RuntimeException("参数list不能为空");
        }
        Class<?> clazz = list.get(0).getClass();
        ExcelMate mate = mateDataParser.parseExcelMate(clazz);
        mateDataParser.parseExcelData(mate, list);

        if (StringUtils.isEmpty(mate.getTpl())) {
            doExport(mate, list);
        }
        else if (isHSSF(mate.getTpl())) {
            doExportByHSSFTpl(mate, list);
            mate.setHssf(true);
        } else {
            doExportBySXSSFTpl(mate, list);
            mate.setHssf(false);
        }

        return mate;
    }

    private void doExport(ExcelMate mate, List<?> list) {
        Workbook workbook;
        if (list.size() > 65534) {
            workbook = new SXSSFWorkbook();
            mate.setHssf(false);
        } else {
            workbook = new HSSFWorkbook();
            mate.setHssf(true);
        }

        ByteArrayOutputStream output = null;
        try {
            Sheet sheet = workbook.createSheet("Sheet1");
            Map<String, CellStyle> columnStyles = Maps.newHashMap();
            Row header = sheet.createRow(0);
            if (mate.getHeaderHeight() > 0) {
                header.setHeight(mate.getHeaderHeight());
            }
            CellStyle headerStyle = getHeaderStyle(mate, workbook);
            for (int i = 0; i < mate.getHeaders().size(); i++) {
                Column column = mate.getHeaders().get(i);
                columnStyles.put(column.getColumnId(), getColumnStyle(column, workbook));
                if (column.getWidth() > 0) {
                    sheet.setColumnWidth(i, column.getWidth());
                }
                Cell cell = header.createCell(i);
                cell.setCellStyle(headerStyle);
                cell.setCellValue(column.getColumnName());
            }
            writeDate(1, sheet, columnStyles, mate.getRows());
            output = new ByteArrayOutputStream();
            workbook.write(output);
            mate.setData(output.toByteArray());
        } catch (IOException e) {
            LOGGER.error("excel导出失败", e);
            throw new RuntimeException("excel导出失败：", e);
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
                if (workbook != null) {
                    workbook.close();
                }
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    private void doExportByHSSFTpl(ExcelMate mate, List<?> list) {
        String tplName = mate.getTpl().startsWith("/") ? mate.getTpl() : ("/" + mate.getTpl());
        InputStream is = getClass().getResourceAsStream(tplName);
        if (is == null) {
            throw new RuntimeException("模板文件："+ mate.getTpl() + "不存在");
        }
        Workbook workbook = null;
        ByteArrayOutputStream output = null;
        try {
            workbook = new HSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            int startRow = 1;
            while (sheet.getRow(startRow) != null) {
                startRow++;
            }
            Map<String, CellStyle> columnStyles = Maps.newHashMap();
            for (int i = 0; i < mate.getHeaders().size(); i++) {
                Column column = mate.getHeaders().get(i);
                columnStyles.put(column.getColumnId(), getColumnStyle(column, workbook));
            }
            writeDate(startRow, sheet, columnStyles, mate.getRows());
            output = new ByteArrayOutputStream();
            workbook.write(output);
            mate.setData(output.toByteArray());
        } catch (Exception e) {
            LOGGER.error("excel导出失败", e);
            throw new RuntimeException("excel导出失败", e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (output != null) {
                    output.close();
                }
                if (workbook != null) {
                    workbook.close();
                }
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    private void doExportBySXSSFTpl(ExcelMate mate, List<?> list) {
        String tplName = mate.getTpl().startsWith("/") ? mate.getTpl() : ("/" + mate.getTpl());
        InputStream is = getClass().getResourceAsStream(tplName);
        if (is == null) {
            throw new RuntimeException("模板文件："+ mate.getTpl() + "不存在");
        }

        XSSFWorkbook tempWb = null;
        SXSSFWorkbook tplWb = null;
        ByteArrayOutputStream output = null;
        try {
            tempWb = new XSSFWorkbook(is);
            tplWb = new SXSSFWorkbook(tempWb);
            SXSSFSheet sXSSFSheet = tplWb.getSheetAt(0);
            int startRow = 1;
            while (sXSSFSheet.getRow(startRow) != null) {
                startRow++;
            }
            Map<String, CellStyle> columnStyles = Maps.newHashMap();
            for (int i = 0; i < mate.getHeaders().size(); i++) {
                Column column = mate.getHeaders().get(i);
                columnStyles.put(column.getColumnId(), getColumnStyle(column, (Workbook)tplWb));
            }
            writeDate(startRow, (Sheet)sXSSFSheet, columnStyles, mate.getRows());
            output = new ByteArrayOutputStream();
            tplWb.write(output);
            mate.setData(output.toByteArray());
        } catch (Exception e) {
            LOGGER.error("excel导出失败", e);
            throw new RuntimeException("excel导出失败", e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (output != null) {
                    output.close();
                }
                if (tplWb != null) {
                    tplWb.close();
                }
                if (tempWb != null) {
                    tempWb.close();
                }
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }


    private void writeDate(int startRow, Sheet sheet, Map<String, CellStyle> columnStyles, List<ExcelRowMate> rowMates) {
        int rowIndex = startRow;
        for (ExcelRowMate rowMate : rowMates) {
            Row row = sheet.createRow(rowIndex);
            for (int i = 0; i < rowMate.getColumns().size(); i++) {
                Column column = rowMate.getColumns().get(i);
                Object value = column.getValue();
                if (value != null) {


                    CellStyle columnStyle = columnStyles.get(column.getColumnId());
                    Cell cell = row.createCell(i);
                    cell.setCellStyle(columnStyle);
                    if (StringUtils.isNotEmpty(column.getExEl())) {
                        value = parserExEl(column.getExEl(), rowMate.getRaw());
                    }
                    if (value instanceof Number) {
                        String str = String.valueOf(value);
                        if (StringUtils.isNotEmpty(column.getFormat())) {
                            str = numberFormat(column.getFormat(), value);
                        }
                        Double number = Double.valueOf(str);
                        cell.setCellValue(number.doubleValue());
                    } else if (value instanceof Date) {
                        Date date = (Date)value;
                        if (StringUtils.isNotEmpty(column.getFormat())) {
                            String formated = dateFormat(column.getFormat(), date);
                            cell.setCellValue(formated);
                        } else {
                            cell.setCellValue(date);
                        }
                    } else if (value instanceof Calendar) {
                        Calendar calendar = (Calendar)value;
                        cell.setCellValue(calendar);
                    } else {
                        String string = String.valueOf(value);
                        cell.setCellValue(string);
                    }
                }
            }  rowMate = null;
            rowIndex++;
        }
    }

    public <T> List<T> doImport(Class<T> clazz, InputStream is, String fileName, int startRow) {
        List<T> list = Lists.newLinkedList();
        Workbook workbook = null;
        try {
            if (isHSSF(fileName)) {
                workbook = new HSSFWorkbook(is);
            } else {
                workbook = new XSSFWorkbook(is);
            }
            ExcelMate mate = mateDataParser.parseExcelMate(clazz);
            Sheet sheet = null;
            Row row = null;
            Cell cell = null;
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                sheet = workbook.getSheetAt(i);
                for (int j = startRow; j <= sheet.getLastRowNum(); j++) {
                    row = sheet.getRow(j);
                    T object = newInstance(clazz);
                    for (Column column : mate.getHeaders()) {
                        LOGGER.info("导入顺序：{} - {}", Integer.valueOf(column.getIndex()), column.getColumnName());
                        cell = row.getCell(column.getIndex());
                        Object value = getCellValue(cell);
                        if (StringUtils.isNotEmpty(column.getImEl())) {
                            value = parserInEl(column.getImEl(), value);
                        }
                        if (value instanceof Double) {
                            value = doubleToDefinite(value, column.getFieldType());
                        }
                        if (isDateType(column.getFieldType()) &&
                                StringUtils.isNotEmpty(column.getFormat())) {
                            value = parseDate(value, column);
                        }

                        try {
                            Method write = getWriteMethod(mate.getClazz(), (Set)mate.getSuperclazz(), column.getFieldName(), new Class[] { column.getFieldType() });
                            write.invoke(object, new Object[] { value });
                        } catch (Exception e) {
                            throw new RuntimeException("设置属性值："+ column.getFieldName() + "失败", e);
                        }
                    }
                    list.add(object);
                }
            }
        } catch (Exception e) {
            LOGGER.error("excel导出失败", e);
            throw new RuntimeException("excel导出失败", e);
        } finally {
            try {
                if (workbook != null) {
                    workbook.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        return list;
    }


    private String numberFormat(String format, Object value) { return String.format(format, new Object[] { value }); }


    private Object parserExEl(String el, Object object) {
        StandardEvaluationContext ctx = new StandardEvaluationContext();
        ctx.setRootObject(object);
        SpelExpression spelExpression = parser.parseRaw(el);
        return spelExpression.getValue(ctx);
    }

    private Object parserInEl(String el, Object value) {
        StandardEvaluationContext ctx = new StandardEvaluationContext();
        ctx.setVariable("import", value);
        LOGGER.info("Spring EL is [{}] value is [{}]", el, value);
        SpelExpression spelExpression = parser.parseRaw(el);
        return spelExpression.getValue(ctx);
    }

    private Object getCellValue(Cell cell) {
        Object value = null;
        switch (cell.getCellType()) {
            case 3:
                value = "";
                break;
            case 4:
                value = Boolean.valueOf(cell.getBooleanCellValue());
                break;
            case 0:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    value = cell.getDateCellValue(); break;
                }
                value = Double.valueOf(cell.getNumericCellValue());
                break;

            case 2:
                value = cell.getCellFormula();
                break;
            case 1:
                value = cell.getStringCellValue();
                break;
        }


        return value;
    }

    private Object doubleToDefinite(Object value, Class<?> targetType) {
        Double d = (Double)value;
        String typeName = targetType.getName();
        if ("int".equals(typeName)) {
            return Integer.valueOf(d.intValue());
        }
        if ("java.lang.Integer".equals(typeName)) {
            return Integer.valueOf(d.intValue());
        }
        if ("float".equals(typeName)) {
            return Float.valueOf(d.floatValue());
        }
        if ("java.lang.Float".equals(typeName)) {
            return Float.valueOf(d.floatValue());
        }
        if ("long".equals(typeName)) {
            return Long.valueOf(d.longValue());
        }
        if ("java.lang.Long".equals(typeName)) {
            return Long.valueOf(d.longValue());
        }
        if ("short".equals(typeName)) {
            return Short.valueOf(d.shortValue());
        }
        if ("java.lang.Short".equals(typeName)) {
            return Short.valueOf(d.shortValue());
        }
        if ("java.math.BigDecimal".equals(typeName)) {
            return new BigDecimal(d.doubleValue());
        }
        if ("java.math.BigInteger".equals(typeName)) {
            return (new BigDecimal(d.doubleValue())).toBigInteger();
        }
        return value;
    }

    private boolean isDateType(Class<?> targetType) {
        String typeName = targetType.getName();
        if ("java.util.Date".equals(typeName) || "java.sql.Date".equals(typeName) ||
                "java.sql.Timestamp".equals(typeName) || "java.sql.Time".equals(typeName)) {
            return true;
        }
        return false;
    }
    private Object parseDate(Object value, Column column) {
        SimpleDateFormat simpleFormat;
        String typeName = column.getFieldType().getName();
        String str = String.valueOf(value);

        Object ret = null;
        if ("yyyy-MM-dd".equals(column.getFormat())) {
            simpleFormat = dayFormat;
        } else if ("yyyy-MM-dd HH:mm".equals(column.getFormat())) {
            simpleFormat = dayTimeFormat;
        } else if ("yyyy-MM-dd HH:mm:ss".equals(column.getFormat())) {
            simpleFormat = dayTimeSecondFormat;
        } else {
            simpleFormat = new SimpleDateFormat(column.getFormat());
        }
        try {
            Date date = simpleFormat.parse(str);
            if ("java.util.Date".equals(typeName)) {
                ret = date;
            }
            if ("java.sql.Date".equals(typeName)) {
                ret = new Date(date.getTime());
            }
            if ("java.sql.Timestamp".equals(typeName)) {
                ret = new Timestamp(date.getTime());
            }
            if ("java.sql.Time".equals(typeName)) {
                ret = new Time(date.getTime());
            }
        } catch (ParseException e) {
            LOGGER.error("日期转换失败", e);
            throw new RuntimeException("日期转换失败", e);
        }
        return ret;
    }

    private String dateFormat(String format, Date value) {
        SimpleDateFormat defaultFormat = dayTimeSecondFormat;
        if (StringUtils.isNotEmpty(format)) {
            if ("yyyy-MM-dd".equals(format)) {
                defaultFormat = dayFormat;
            } else if ("yyyy-MM-dd HH:mm".equals(format)) {
                defaultFormat = dayTimeFormat;
            } else {
                defaultFormat = new SimpleDateFormat(format);
            }
        }
        return defaultFormat.format(value);
    }

    private CellStyle getHeaderStyle(ExcelMate mate, Workbook wb) {
        CellStyle style = wb.createCellStyle();
        if (mate.getHeaderAlign() == 1) {
            style.setAlignment(HorizontalAlignment.LEFT);
        } else if (mate.getHeaderAlign() == 3) {
            style.setAlignment(HorizontalAlignment.RIGHT);
        } else {
            style.setAlignment(HorizontalAlignment.CENTER);
        }

        Font font = wb.createFont();
        if (StringUtils.isNotEmpty(mate.getHeaderFontName())) {
            font.setFontName(mate.getHeaderFontName());
        }
        if (mate.isHeaderBold()) {
            font.setBold(true);
        }
        if (mate.getHeaderFontSize() > 0) {
            font.setFontHeightInPoints(mate.getHeaderFontSize());
        }
        style.setFont(font);
        return style;
    }

    private CellStyle getColumnStyle(Column column, Workbook wb) {
        CellStyle style = wb.createCellStyle();
        if (column.getAlign() == 1) {
            style.setAlignment(HorizontalAlignment.LEFT);
        } else if (column.getAlign() == 3) {
            style.setAlignment(HorizontalAlignment.RIGHT);
        } else if (column.getAlign() == 2) {
            style.setAlignment(HorizontalAlignment.CENTER);
        }

        Font font = wb.createFont();
        if (StringUtils.isNotEmpty(column.getFontName())) {
            font.setFontName(column.getFontName());
        }
        if (column.isBold()) {
            font.setBold(true);
        }
        if (column.getFontSize() > 0) {
            font.setFontHeightInPoints(column.getFontSize());
        }
        style.setFont(font);

        return style;
    }

    private boolean isHSSF(String fileName) {
        return fileName.toLowerCase().contains("xlsx");
    }

    private Method getWriteMethod(Class<?> cls, Set<Class<?>> superclass, String fieldName, Class... parameterTypes) {
        Set<Class<?>> classes = Sets.newHashSet();
        classes.add(cls);
        classes.addAll(superclass);
        String methodName = "set" + capitalize(fieldName);
        return getMethod(classes, methodName, parameterTypes);
    }

    private Method getMethod(Set<Class<?>> classes, String methodName, Class... parameterTypes) {
        for (Class<?> i : classes) {
            try {
                Method method = i.getDeclaredMethod(methodName, parameterTypes);
                if ((!Modifier.isPublic(method.getModifiers()) ||
                        !Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()) {
                    method.setAccessible(true);
                }
                return method;
            } catch (NoSuchMethodException noSuchMethodException) {}
        }

        return null;
    }

    private String capitalize(String fieldName) {
        char[] chars = fieldName.toCharArray();
        if (chars[0] >= 'a' && chars[0] <= 'z') {
            chars[0] = (char)(chars[0] - 32);
        }
        return new String(chars);
    }

    private <T> T newInstance(Class<?> cls) {
        if (cls.isInterface()) {
            throw new IllegalArgumentException("不是有效的类型");
        }
        if (INSTANTIATORS.contains(cls.getName())) {
            return (T)((ObjectInstantiator)INSTANTIATORS.get(cls.getName())).newInstance();
        }
        ObjectInstantiator<?> instantiator = OBJENESIS.getInstantiatorOf(cls);
        INSTANTIATORS.putIfAbsent(cls.getName(), instantiator);
        return (T)instantiator.newInstance();
    }
}

