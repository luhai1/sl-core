package com.sl.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
public class FileUtil {
    private FileUtil() {

    }

    /**
     * @param file
     *            //文件对象
     * @param filePath
     *            //上传路径
     * @param fileName
     *            //文件名
     * @return 文件名
     */
    public static String fileUpload(MultipartFile file, String filePath, String fileName) {
        String extName = ""; // 扩展名格式：

        File file2 = new File(filePath);
        // 如果文件夹不存在则创建
        if (!file2.exists() && !file2.isDirectory()) {
            file2.mkdir();
        }

        try {
            if (file.getOriginalFilename().lastIndexOf(System.lineSeparator()) >= 0) {
                extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            }
            // copyFile(file.getInputStream(), filePath, fileName + extName).replaceAll("-", "");
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
        }
        return fileName + extName;
    }

}
