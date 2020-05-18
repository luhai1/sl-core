package com.sl.common.excel.service;

import com.sl.common.excel.innter.ExcelMate;
import com.sl.common.excel.innter.WorkbookParser;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class ExcelService {

    private final WorkbookParser wbParser = new WorkbookParser();


    public ExcelMate export(List<?> list) {
        return this.wbParser.export(list);
    }


    public <T> List<T> doImport(Class<T> clazz, MultipartFile file) {
        try {
            InputStream is = file.getInputStream();
            return this.wbParser.doImport(clazz, is, file.getOriginalFilename(), 1);
        } catch (IOException e) {
            throw new RuntimeException("Excel导入失败", e);
        }
    }

    public <T> List<T> doImport(Class<T> clazz, MultipartFile file, int startRow) {
        try {
            InputStream is = file.getInputStream();
            return this.wbParser.doImport(clazz, is, file.getOriginalFilename(), startRow);
        } catch (IOException e) {
            throw new RuntimeException("Excel导入失败" , e);
        }
    }


    public <T> List<T> doImport(Class<T> clazz, InputStream is, String fileName, int startRow) {
        return this.wbParser.doImport(clazz, is, fileName, startRow);
    }
}
