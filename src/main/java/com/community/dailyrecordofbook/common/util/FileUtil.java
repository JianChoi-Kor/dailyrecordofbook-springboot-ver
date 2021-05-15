package com.community.dailyrecordofbook.common.util;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class FileUtil {

    private ServletContext ctx;

    private void makeFolders(String path) {
        File folder = new File(path);
        if(!folder.exists()) {
            folder.mkdirs();
        }
    }

//    public String getBasePath(String... moreFolder) {
//        String temp = "";
//        for (String s : moreFolder) {
//            temp += s;
//        }
//        String basePath = ctx.getRealPath(temp);
//        return basePath;
//    }

    private String getExt(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private String getRandomFileName(String fileName) {
        return UUID.randomUUID().toString() + "." + getExt(fileName);
    }

    public String transferTo(MultipartFile mf, String basePath) {
        String fileName = null;
        makeFolders(basePath);

        try {
            fileName = getRandomFileName(mf.getOriginalFilename());
            File file = new File(basePath, fileName);
            mf.transferTo(file);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return fileName;
    }

}
