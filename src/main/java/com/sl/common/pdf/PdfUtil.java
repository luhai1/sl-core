package com.sl.common.pdf;


import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.canvas.draw.DottedLine;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.TabAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import com.itextpdf.text.pdf.parser.ContentByteUtils;
import com.itextpdf.text.pdf.parser.PdfContentStreamProcessor;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.sl.common.i18n.LocaleMessageSource;
import com.sl.constant.NumberConstant;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

public class PdfUtil {


    /**
     * 生成pdf文件
     * @param pdfData pdf数据
     * @param response 输出流
     */
    public static void generateToPdf(PdfData pdfData, HttpServletResponse response){
        OutputStream responseStream = null;
        // 临时pdf文件存放
        String tmpStr = PdfConstant.PDF_TEMP_PATH;
        String fileName = System.currentTimeMillis()+"_temp.pdf";
        File file = new File(tmpStr + File.separator + fileName);
        try {
            OutputStream out = null;
            if(CollectionUtils.isNotEmpty(pdfData.getPdfFtlDataList())){
                String html = getPdfContent(pdfData.getPdfFtlDataList().get(0));
                if (file.exists() || ((file.getParentFile().exists() || file.getParentFile().mkdirs()) && file.createNewFile())) {
                    out = new FileOutputStream(file);
                }
                ITextRenderer render = null;
                render = getRender();
                render.setDocumentFromString(html);
                render.layout();
                render.createPDF(out,false);
                for(int i=1;i<pdfData.getPdfFtlDataList().size();i++){
                    String tempHtml = getPdfContent(pdfData.getPdfFtlDataList().get(i));
                    render.setDocumentFromString(tempHtml);
                    render.layout();
                    render.writeNextDocument();
                }
                render.finishPDF();
                out.flush();
                out.close();
                FileInputStream is = new FileInputStream(tmpStr + File.separator + fileName);
                // 加水印
                byte[] bytes = IOUtils.toByteArray(is);
                // 加图片水印
                bytes = addWaterImage(bytes,pdfData);
                // 加文字水印
                bytes = addWaterMark(bytes,pdfData);
                // 加页眉页脚
                bytes = addHeaderFooter(bytes,pdfData);
                // 加目录
                if(pdfData.isNeedCatalog()){
                    bytes =  createPdfWithCatalog(bytes,pdfData.getPdfCatalog());
                }
                responseStream= response.getOutputStream();
                responseStream.write(bytes);
                responseStream.flush();
                response.setContentType("application/pdf");
                response.setCharacterEncoding("UTF-8");
                String outFileName = pdfData.getOutPdfName();
                if(StringUtils.isEmpty(outFileName)){
                    outFileName = System.currentTimeMillis()+"";
                }
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(outFileName, "UTF-8") + ".pdf");

                is.close();
            }else{
                throw new RuntimeException(LocaleMessageSource.getMessage(PdfConstant.PDF_FTL_DATA_EMPTY));
            }

        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }finally {
            if( null != responseStream){
                try {
                    responseStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            file.delete();
        }
    }

    public static ITextRenderer getRender() throws IOException, com.lowagie.text.DocumentException {
        ITextRenderer render = new ITextRenderer();
        // 添加字体，以支持中文
        render.getFontResolver().addFont( PdfConstant.PDF_ARIALUNI_PATH, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        render.getFontResolver().addFont( PdfConstant.PDF_SIMSUN_PATH, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        return render;
    }


    // 获取要写入PDF的内容
    public static String getPdfContent(PdfFtlData pdfFtlData) throws Exception {
        return useTemplate(pdfFtlData.getTemplatePath(), pdfFtlData.getTemplateFileName(), pdfFtlData.getTemplateData());
    }


    // 使用freemarker得到html内容
    public static String useTemplate(String ftlPath, String ftlName, Object o) throws Exception {
        String html = null;
        Template tpl = getFreemarkerConfig(ftlPath).getTemplate(ftlName);
        tpl.setEncoding("UTF-8");
        StringWriter writer = new StringWriter();
        tpl.process(o, writer);
        writer.flush();
        html = writer.toString();
        return html;
    }


    /**
     * 获取Freemarker配置
     *
     * @param templatePath
     * @return
     * @throws IOException
     */
    private static Configuration getFreemarkerConfig(String templatePath) throws Exception {
        Configuration config = new Configuration();
        config.setTemplateLoader(new ClassTemplateLoader(PdfUtil.class.getClassLoader(), templatePath));
        //config.setDirectoryForTemplateLoading();
        config.setEncoding(Locale.CHINA, "utf-8");
        return config;
    }

    /**
     * 加水印文字
     * @param pdfByte
     * @param pdfData
     * @return
     */

    public static byte[] addWaterMark(byte[] pdfByte,PdfData pdfData){
        if(null == pdfData){
            return pdfByte;
        }
        ByteArrayOutputStream out = null;
        PdfReader pdfReader = null;
        PdfStamper pdfStamper = null;
        try {
             pdfReader = new PdfReader(pdfByte);
            out = new ByteArrayOutputStream();
            pdfStamper = new PdfStamper(pdfReader, out);

            PdfContentByte waterContent = null;
            if(pdfData.isNeedWaterMark()){
                for(int i = 1; i <= pdfReader.getNumberOfPages(); i++){
                    waterContent = pdfStamper.getUnderContent(i);//在内容上方加水印
                    setWaterMark(waterContent,pdfData.getWaterMark());
                }
            }else if(CollectionUtils.isNotEmpty(pdfData.getPdfFtlDataList())){
                // 为单独需要加水印文字的加上
                for(int i = 0; i<pdfData.getPdfFtlDataList().size(); i++){
                    if(pdfData.getPdfFtlDataList().get(i).isNeedWaterMark()){
                        waterContent = pdfStamper.getUnderContent(i+1);//在内容上方加水印
                        setWaterMark(waterContent,pdfData.getPdfFtlDataList().get(i).getWaterMark());
                    }
                }
            }
             pdfStamper.close();
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }finally {
            if (pdfReader != null)
                pdfReader.close();
        }
        return out.toByteArray();
    }



    /**
     * 加图片水印
     * @param pdfByte pdf字节
     * @param pdfData pdf数据
     * @return byte[]
     */
    public static byte[] addWaterImage(byte[] pdfByte,PdfData pdfData) {
        if(null == pdfData){
            return pdfByte;
        }
        PdfReader reader = null;
        PdfStamper stamp = null;
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            reader = new PdfReader(pdfByte);
            stamp = new PdfStamper(reader, out);
            // 是否需要设置全部页水印
            if(pdfData.isNeedWaterImage()){
               WaterImage waterImage = pdfData.getWaterImage();
               for(int i = 1; i <= reader.getNumberOfPages(); i++){
                   PdfContentByte pdfContentByte = stamp.getUnderContent(i);
                   // 设置水印
                   setImageWater(pdfContentByte,waterImage);
               }
            }else{
                // 为需要加水印页的单独设置水印
                if(CollectionUtils.isNotEmpty(pdfData.getPdfFtlDataList())){
                    for(int i = 0; i < pdfData.getPdfFtlDataList().size(); i++){
                        if(pdfData.getPdfFtlDataList().get(i).isNeedWaterImage()){
                            PdfContentByte pdfContentByte = stamp.getUnderContent(i+1);
                            setImageWater(pdfContentByte,pdfData.getPdfFtlDataList().get(i).getWaterImage());
                        }
                    }
                }


            }
            stamp.close();
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }finally {
            if(null != reader)
            reader.close();
        }
        return out.toByteArray();
    }


    private static byte[] addHeaderFooter(byte[] pdfByte,PdfData pdfData) throws Exception {

        if(null == pdfData){
            return pdfByte;
        }
        PdfReader reader = null;
        PdfStamper stamp = null;
        ByteArrayOutputStream out = null;
        PdfContentByte pdfContentByte = null;
        try {
            out = new ByteArrayOutputStream();
            reader = new PdfReader(pdfByte);
            stamp = new PdfStamper(reader, out);
            // 是否需要设置全部页水印
            if(pdfData.isNeedHeader() || pdfData.isNeedFooter()){

                for(int i = 1; i <= reader.getNumberOfPages(); i++){
                    PdfDocument pdfDocument = stamp.getUnderContent(i).getPdfDocument();
                    pdfContentByte= stamp.getUnderContent(i);
                    // 设置页眉页脚
                    setHeaderFooter(pdfContentByte,pdfDocument,pdfData,i);
                }
            }
            stamp.close();
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }finally {
            if(null != reader)
                reader.close();
        }
        return out.toByteArray();
    }

    private static void setHeaderFooter(PdfContentByte pdfContentByte,Document document,PdfData pdfData,int page) throws IOException, DocumentException {
        BaseFont baseFont;
        if(pdfData.isNeedHeader()){
            if(null != pdfData.getPdfHeader()){
                PdfHeader pdfHeader = pdfData.getPdfHeader();
                baseFont = pdfHeader.getFont();
                if(null == baseFont){
                    baseFont =  BaseFont.createFont("STSong-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
                }
                Font font = new Font(baseFont,pdfHeader.getSize(),pdfHeader.getStyle());
                Phrase phrase = new Phrase(pdfHeader.getHeaderText(),font);
                if(null != pdfHeader.getImage()){
                    phrase.add(new Chunk(pdfHeader.getImage(), pdfHeader.getImageX(), pdfHeader.getImageY()));
                }

                // 1、写入左侧页眉
                ColumnText.showTextAligned(pdfContentByte,
                        pdfHeader.getAlignment(), phrase,
                        document.right(pdfHeader.getRightSpace()), document.top(pdfHeader.getTopSpace()), pdfHeader.getRotation());

            }

        }
        if(pdfData.isNeedFooter()){
            PdfFooter pdfFooter = pdfData.getPdfFooter();
            baseFont = pdfFooter.getFont();
            if(null == baseFont){
                baseFont =  BaseFont.createFont("STSong-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
            }
            Font font = new Font(baseFont,pdfFooter.getSize(),pdfFooter.getStyle());
            Phrase phrase = new Phrase(pdfFooter.getHeaderText()+page,font);
            if(null != pdfFooter.getImage()){
                phrase.add(new Chunk(pdfFooter.getImage(), pdfFooter.getImageX(), pdfFooter.getImageY()));
            }
            ColumnText.showTextAligned(pdfContentByte,
                    pdfFooter.getAlignment(), phrase,
                    document.left(pdfFooter.getLeftSpace()), document.bottom(pdfFooter.getBottomSpace()), pdfFooter.getRotation());

        }
    }

    /**
     * 文字水印
     * @param waterContent 加入水印的页
     * @param waterMark 水印信息
     */
    private   static void setWaterMark(PdfContentByte waterContent , WaterMark waterMark) throws IOException, com.itextpdf.text.DocumentException {
        if(null == waterContent || null == waterMark){
          return;
        }
        PdfGState pdfGState = new PdfGState();
        //透明度
        pdfGState.setFillOpacity(waterMark.getFillOpacity());// 设置透明度

        //开始写入文本
        waterContent.beginText();
        waterContent.setGState(pdfGState);
        // 设置水印颜色
        waterContent.setColorFill(waterMark.getColorFill());
        // 设置水印字体参数及大小
        BaseFont baseFont = waterMark.getBasefont();
        if(null == baseFont){
            baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        }
        waterContent.setFontAndSize(baseFont, waterMark.getSize());
        // 设置水印对齐方式 水印内容 X坐标 Y坐标 旋转角度
        waterContent.showTextAligned(waterMark.getAlignment()
                , waterMark.getWaterText(), waterMark.getWaterX(),waterMark.getWaterY(), waterMark.getRotation());

        waterContent.endText();
    }

    /**
     * 加入图片水印
     * @param pdfContentByte 加入水印的页
     * @param waterImage 水印信息
     */
     private static void setImageWater(PdfContentByte pdfContentByte,WaterImage waterImage) throws com.itextpdf.text.DocumentException, IOException {
         if(null == pdfContentByte || null == waterImage){
             return;
         }
         PdfGState pdfGState = new PdfGState();
         // 设置透明度
         pdfGState.setFillOpacity(waterImage.getFillOpacity());

         Image image = Image.getInstance(IOUtils.toByteArray(new ClassPathResource(waterImage.getImagePath()).getInputStream()));

         image.setAbsolutePosition(waterImage.getAbsoluteX(),waterImage.getAbsoluteY());

         /* 设置图片的大小*/
         if(NumberConstant.NUMBER_ZERO.floatValue() != waterImage.getScaleAbsoluteWidth() && 0 != waterImage.getScaleAbsoluteHeight()){
             image.scaleAbsolute(waterImage.getScaleAbsoluteWidth(), waterImage.getScaleAbsoluteHeight()); // 设置宽高
         }else if(NumberConstant.NUMBER_100.floatValue()  != waterImage.getScalePercent()){
             image.scalePercent(waterImage.getScalePercent()); // 设置等比例放缩
         }
         // 设置旋转弧度
         if(NumberConstant.NUMBER_ZERO.floatValue() != waterImage.getRotation()){
             image.setRotation(waterImage.getRotation());
         }
         // 设置旋转角度
         if(NumberConstant.NUMBER_ZERO.floatValue() != waterImage.getRotationDegrees()){
             image.setRotationDegrees(waterImage.getRotationDegrees());
         }
         pdfContentByte.setGState(pdfGState);
         pdfContentByte.addImage(image);
     }



     private static byte[] createPdfWithCatalog(byte[] pdfByte,  PdfCatalog pdfCatalog){
         if(null == pdfCatalog || org.springframework.util.CollectionUtils.isEmpty(pdfCatalog.getCatalogs())){
             return pdfByte;
         }
         //实际页码索引偏移量为 目录占页数-1
         int offset = pdfCatalog.getCatalogPageSize() - 1;
         for (Map.Entry<String, Integer> entry : pdfCatalog.getCatalogs().entrySet()) {
             entry.setValue(entry.getValue() + offset);
         }
         ByteArrayOutputStream out = null;
         try{
             out = new ByteArrayOutputStream();
             com.itextpdf.kernel.pdf.PdfDocument pdfDocument = new com.itextpdf.kernel.pdf.PdfDocument(new com.itextpdf.kernel.pdf.PdfWriter(out));
             com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdfDocument);
             //处理中文问题
             com.itextpdf.kernel.font.PdfFont font = PdfFontFactory.createFont(PdfConstant.PDF_SIMSUN_PATH+",0", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
             String catalogTitle = pdfCatalog.getCatalogTitle();
             if (catalogTitle== null) {
                 catalogTitle = "";
             }
             document.add(new com.itextpdf.layout.element.Paragraph(new Text(catalogTitle))
                     .setTextAlignment(TextAlignment.CENTER).setFont(font));
             com.itextpdf.kernel.pdf.PdfDocument firstSourcePdf = new com.itextpdf.kernel.pdf.PdfDocument(new com.itextpdf.kernel.pdf.PdfReader(new ByteArrayInputStream(pdfByte)));
             int numberOfPages = firstSourcePdf.getNumberOfPages();
             for (int i = 1; i <= numberOfPages; i++) {
                 com.itextpdf.kernel.pdf.PdfPage page = firstSourcePdf.getPage(i).copyTo(pdfDocument);
                 pdfDocument.addPage(page);
                 for (Map.Entry<String, Integer> entry : pdfCatalog.getCatalogs().entrySet()) {
                     if (i == entry.getValue()) {
                         String destinationKey = "p" + (pdfDocument.getNumberOfPages() - 1);
                         com.itextpdf.kernel.pdf.PdfArray destinationArray = new com.itextpdf.kernel.pdf.PdfArray();
                         destinationArray.add(page.getPdfObject());
                         destinationArray.add(com.itextpdf.kernel.pdf.PdfName.XYZ);
                         destinationArray.add(new com.itextpdf.kernel.pdf.PdfNumber(0));
                         destinationArray.add(new com.itextpdf.kernel.pdf.PdfNumber(page.getMediaBox().getHeight()));
                         destinationArray.add(new com.itextpdf.kernel.pdf.PdfNumber(1));
                         pdfDocument.addNamedDestination(destinationKey, destinationArray);
                         com.itextpdf.layout.element.Paragraph p = new com.itextpdf.layout.element.Paragraph();
                         p.setFont(font);
                         p.addTabStops(new com.itextpdf.layout.element.TabStop(540, TabAlignment.RIGHT, new DottedLine()));
                         p.add(entry.getKey());
                         p.add(new com.itextpdf.layout.element.Tab());
                         p.add(String.valueOf(pdfDocument.getNumberOfPages() - 1));
                         p.setProperty(Property.ACTION, com.itextpdf.kernel.pdf.action.PdfAction.createGoTo(destinationKey));
                         document.add(p);
                     }
                 }
             }
             firstSourcePdf.close();
             document.close();
             out.flush();

         }catch (Exception e){
             e.printStackTrace();
             throw new RuntimeException(e.getMessage());
         }finally {
             if(null != out){
                 try {
                     out.close();
                 } catch (IOException e) {
                     e.printStackTrace();
                     throw new RuntimeException(e.getMessage());
                 }
             }
         }
         return out.toByteArray();
     }

    /**
     * 去除pdf中水印
     */

    public static void removePdfWaterMarked(String watermarkedFile) throws IOException, DocumentException {

    }

    public static void main(String[] aargs) throws Exception {
        removeWatermark("D:\\test.pdf","D:\\1.pdf");
    }
    public static void removeWatermark(String srcPath, String buildPath) throws Exception {
        PdfReader reader = new PdfReader( IOUtils.toByteArray(new FileInputStream(new File(srcPath))));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfStamper stamp  = new PdfStamper(reader, out);
        System.out.println(reader.getNumberOfPages());
        for(int i = 1; i <= reader.getNumberOfPages(); i++){
            PdfContentByte pdfContentByte = stamp.getUnderContent(i);
            if(null != pdfContentByte){
                // 设置水印
                Image image =  Image.getInstance(IOUtils.toByteArray(new ClassPathResource("/templates/ftl/115121.jpg").getInputStream()));
                image.setAbsolutePosition(250,250);
                pdfContentByte.addImage(image);
            }

        }
        stamp.close();
        reader.close();
        OutputStream outputStream = new FileOutputStream(new File(buildPath)) ;
        outputStream.write(out.toByteArray());
        outputStream.flush();
        outputStream.close();
    }


}
