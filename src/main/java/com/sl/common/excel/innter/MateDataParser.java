package com.sl.common.excel.innter;


import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sl.common.excel.annotation.Excel;
import com.sl.common.excel.annotation.ExcelColumn;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;


public class MateDataParser {
    public ExcelMate parseExcelMate(Class<?> clazz) {
        Excel excel = clazz.getAnnotation(Excel.class);
        if (Objects.isNull(excel)) {
            throw new RuntimeException("类注解必须添加Excel注解");
        }

        ExcelMate excelMate = new ExcelMate();
        excelMate.setTpl(excel.tpl());
        excelMate.setClazz(clazz);
        excelMate.setSuperclazz(getSuperclass(clazz));
        excelMate.setName(excel.name());
        excelMate.setHeight(excel.height());
        excelMate.setFontSize(excel.fontSize());
        excelMate.setHeaderHeight(excel.headerHeight());
        excelMate.setHeaderAlign(excel.headerAlign());
        excelMate.setHeaderFontName(excel.headerFontName());
        excelMate.setHeaderFontSize(excel.headerFontSize());
        excelMate.setHeaderBold(excel.headerBold());
        excelMate.setHeaders(parseHeaders(excelMate));
        return excelMate;
    }

    private List<Column> parseHeaders(ExcelMate mate) {
        List<Column> columns = Lists.newArrayList();
        List<Field> fields = getFields(mate.getClazz(), mate.getSuperclazz());

        int i = 0;
        for (Field field : fields) {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            ExcelColumn excelColumn = field.getAnnotation(ExcelColumn.class);
            if (Objects.isNull(excelColumn)) {
                continue;
            }
            String fieldName = field.getName();
            String columnName = excelColumn.name();
            if (StringUtils.isEmpty(columnName)) {
                columnName = fieldName;
            }
            int index = excelColumn.index();
            if (index == 0) {
                index = i;
            }
            Column column = new Column();
            column.setColumnId(UUID.randomUUID().toString());
            column.setFieldName(fieldName);
            column.setFieldType(field.getType());
            column.setColumnName(columnName);
            column.setIndex(index);
            column.setWidth(excelColumn.width());
            column.setFormat(excelColumn.format());
            column.setExEl(excelColumn.exEl());
            column.setImEl(excelColumn.imEl());
            column.setAlign(excelColumn.align());
            column.setBold(excelColumn.bold());
            column.setFontName(excelColumn.fontName());
            column.setFontSize(excelColumn.fontSize());
            column.setHeight(excelColumn.height());
            columns.add(column);
        }
        Collections.sort(columns, new Comparator<Column>() {
            public int compare(Column o1, Column o2) {
                if (o1.getIndex() > o2.getIndex())
                    return 1;
                if (o1.getIndex() < o2.getIndex()) {
                    return -1;
                }
                return 0;
            }
        });
        return columns;
    }

    public void parseExcelData(ExcelMate mate, List<?> list) {
        List<ExcelRowMate> rows = Lists.newLinkedList();
        List<Column> headers = mate.getHeaders();
        for (Object data : list) {
            ExcelRowMate row = new ExcelRowMate();
            row.setRaw(data);
            for (Column header : headers) {
                Method readMethod = getReadMethod(mate.getClazz(), mate.getSuperclazz(), header.getFieldName(), new Class[0]);
                if (Objects.isNull(readMethod)) {
                    throw new RuntimeException("+ header.getFieldName() + ");
                }
                Object value = null;
                try {
                    value = readMethod.invoke(data, new Object[0]);
                } catch (Exception exception) {
                }


                Column column = new Column();
                column.setColumnId(header.getColumnId());
                column.setFormat(header.getFormat());
                column.setExEl(header.getExEl());
                column.setImEl(header.getImEl());
                column.setValue(value);
                row.addColumn(column);
            }
            rows.add(row);
        }
        mate.setRows(rows);
    }

    private String capitalize(String fieldName) {
        char[] chars = fieldName.toCharArray();
        if (chars[0] >= 'a' && chars[0] <= 'z') {
            chars[0] = (char) (chars[0] - 32);
        }
        return new String(chars);
    }

    private Set<Class<?>> getSuperclass(Class<?> cls) {
        Set<Class<?>> classes = new HashSet<>();
        Class<?> superclass = cls.getSuperclass();
        while (superclass != null) {
            classes.add(superclass);
            superclass = superclass.getSuperclass();
        }
        return Collections.unmodifiableSet(classes);
    }

    private List<Field> getFields(Class<?> cls, Set<Class<?>> superclass) {
        List<Field> fields = new LinkedList<>();
        for (Class<?> type : superclass) {
            byte b1;
            int j;
            Field[] arrayOfField1;
            for (j = (arrayOfField1 = type.getDeclaredFields()).length, b1 = 0; b1 < j; ) {
                Field field = arrayOfField1[b1];
                fields.add(field);
                b1++;
            }

        }
        byte b;
        int i;
        Field[] arrayOfField;
        for (i = (arrayOfField = cls.getDeclaredFields()).length, b = 0; b < i; ) {
            Field field = arrayOfField[b];
            fields.add(field);
            b++;
        }

        return Collections.unmodifiableList(fields);
    }

    private Method getReadMethod(Class<?> cls, Set<Class<?>> superclass, String fieldName, Class... parameterTypes) {
        Set<Class<?>> classes = Sets.newHashSet();
        classes.add(cls);
        classes.addAll(superclass);
        String methodName = "get" + capitalize(fieldName);
        Method method = getMethod(classes, methodName, parameterTypes);
        if (method == null) {
            methodName = "is" + capitalize(fieldName);
            method = getMethod(classes, methodName, parameterTypes);
        }
        return method;
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
            } catch (NoSuchMethodException noSuchMethodException) {
            }
        }

        return null;
    }
}
