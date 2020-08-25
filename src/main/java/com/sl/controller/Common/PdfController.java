package com.sl.controller.Common;

import com.sl.common.i18n.LocaleMessageSource;
import com.sl.common.util.WordToPdfUtils;
import com.sl.constant.CommonConstant;
import com.sl.constant.CommonErrorConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@Slf4j
@RequestMapping("/pub/pdf")
public class PdfController {

    @RequestMapping("/wordToPdf")
    public void wordToPdf(MultipartFile file, HttpServletResponse response){

        if(null == file ){
            throw new RuntimeException(LocaleMessageSource.getMessage(CommonErrorConstant.FILE_NOT_EXIT));
        }
        String filePath = file.getOriginalFilename();
        if( !filePath.endsWith(CommonConstant.FILE_TYPE_DOC) && !filePath.endsWith(CommonConstant.FILE_TYPE_DOCX)){
            throw new RuntimeException(LocaleMessageSource.getMessage(CommonErrorConstant.FILE_NOT_DOC));
        }
        try {
            WordToPdfUtils.doc2pdf(file.getInputStream(),response.getOutputStream());
            response.setContentType("application/pdf");
            response.setCharacterEncoding("UTF-8");
        } catch (IOException e) {
            log.error(e.getMessage(),e);
            e.printStackTrace();
        }

    }
}
