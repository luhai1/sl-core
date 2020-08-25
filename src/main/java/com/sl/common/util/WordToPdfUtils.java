package com.sl.common.util;

import com.aspose.words.*;
import com.sl.common.i18n.LocaleMessageSource;
import com.sl.constant.CommonErrorConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.awt.*;
import java.io.*;

@Slf4j
public class WordToPdfUtils {
    public static void main(String[] args) {
        doc2pdf("C:\\Users\\luhai\\Desktop\\处方流转平台\\处方流转平台技术方案.docx", "D:\\word.pdf");
    }

    /**
     * Word 转 PDF
     *
     * @param inPath  word文件地址
     * @param outPath pdf文件地址
     */
    public static void doc2pdf(String inPath, String outPath) {
        File wordFile = new File(inPath);
        if (!wordFile.exists()) {
            log.error(LocaleMessageSource.getMessage(CommonErrorConstant.FILE_NOT_EXIT), inPath);
            throw new RuntimeException(LocaleMessageSource.getMessage(CommonErrorConstant.FILE_NOT_EXIT));
        }

        // 验证License 若不验证则转化出的pdf文档会有水印产生
        if (!getLicense()) {
            throw new RuntimeException(LocaleMessageSource.getMessage(CommonErrorConstant.LISTEN_FILE_NOT_EXIT));
        }
        try {
            //获取文件
            File file = new File(outPath);
            //获取文件流
            FileOutputStream os = new FileOutputStream(file);
            // Address是将要被转化的word文档
            Document doc = new Document(inPath);
            // 全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF
            completeTableBorder(doc);
            doc.save(os, SaveFormat.PDF);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Word 转 PDF
     *
     * @param inPath       word文件地址
     * @param outputStream 输出文件流
     */
    public static void doc2pdf(String inPath, OutputStream outputStream) {
        File wordFile = new File(inPath);
        if (!wordFile.exists()) {
            log.error(LocaleMessageSource.getMessage(CommonErrorConstant.FILE_NOT_EXIT), inPath);
            throw new RuntimeException(LocaleMessageSource.getMessage(CommonErrorConstant.FILE_NOT_EXIT));
        }

        // 验证License 若不验证则转化出的pdf文档会有水印产生
        if (!getLicense()) {
            throw new RuntimeException(CommonErrorConstant.LISTEN_FILE_NOT_EXIT);
        }
        try {
            // Address是将要被转化的word文档
            Document doc = new Document(inPath);
            // 全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF
            completeTableBorder(doc);
            doc.save(outputStream, SaveFormat.PDF);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Word 转 PDF
     *
     * @param inputStream  word文件流
     * @param outputStream 输出文件流
     */
    public static void doc2pdf(InputStream inputStream, OutputStream outputStream) {
        try {
            if (null == inputStream) {
                log.error(LocaleMessageSource.getMessage(CommonErrorConstant.FILE_NOT_EXIT));
                throw new RuntimeException(LocaleMessageSource.getMessage(CommonErrorConstant.FILE_NOT_EXIT));
            }
            // 验证License 若不验证则转化出的pdf文档会有水印产生
            if (!getLicense()) {
                throw new RuntimeException(LocaleMessageSource.getMessage(CommonErrorConstant.LISTEN_FILE_NOT_EXIT));
            }
            FontSettings.setFontsFolder("/usr/share/fonts" + File.separator, true);
            // Address是将要被转化的word文档
            Document doc = new Document(inputStream);
            // 全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF
            completeTableBorder(doc);
            doc.save(outputStream, SaveFormat.PDF);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            e.printStackTrace();
        }finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 控制表格边框不缺失
     *
     * @param doc
     * @throws Exception
     */
    private static void completeTableBorder(Document doc) throws Exception {
        for (Table table : (Iterable<Table>) doc.getChildNodes(NodeType.TABLE, true)) {
            //设置表格居中
            table.setAlignment(TableAlignment.CENTER);
            //设置表格左边框线
            table.setBorder(BorderType.LEFT, LineStyle.SINGLE, 1, Color.black, true);
            //设置表格右边框线
            table.setBorder(BorderType.RIGHT, LineStyle.SINGLE, 1, Color.black, true);
            //设置表格上边框线
            table.setBorder(BorderType.TOP, LineStyle.SINGLE, 1, Color.black, true);
            //设置表格下边框线
            table.setBorder(BorderType.BOTTOM, LineStyle.SINGLE, 1, Color.black, true);
            for (Row row : (Iterable<Row>) table.getChildNodes(NodeType.ROW, true)) {
                for (Cell cell : (Iterable<Cell>) row.getChildNodes(NodeType.CELL, true)) {
                    //设置单元格上下边框
                    cell.getCellFormat().getBorders().getBottom().setLineStyle(LineStyle.SINGLE);
                    cell.getCellFormat().getBorders().getTop().setLineStyle(LineStyle.SINGLE);
                }
            }
        }
    }

    /**
     * 去除水印
     *
     * @return
     */
    private static boolean getLicense() {
        boolean result = false;
        try {
            InputStream is = new ClassPathResource("aspose/license.xml").getInputStream();
            License aposeLic = new License();
            aposeLic.setLicense(is);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}