package com.community.dailyrecordofbook.common.util;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.jpeg.JpegDirectory;
import lombok.RequiredArgsConstructor;
import org.imgscalr.Scalr;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    public String getDate() {
        String pattern = "yyyyMMdd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());
        return date;
    }

    public String transferTo(MultipartFile multipartFile, boolean createThumb, String basePath) throws Exception {
        String writeImage = null;
        makeFolders(basePath);
        File file;
        try {
            writeImage = getRandomFileName(multipartFile.getOriginalFilename());
            file = new File(basePath, writeImage);
            multipartFile.transferTo(file);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (createThumb) {
            makeThumbnail(file, basePath, 500);
        }
        return writeImage;
    }

    private void makeThumbnail(File originFile, String basePath, int MAX) throws Exception {
        String fileName = originFile.getName();
        String extension = getExt(originFile.getName());

        File imageFile = new File(basePath + "/" + fileName);

        int orientation = 1; // 회전정보 1 => 0도, 3 => 180도, 6 => 270도, 8 => 90도

        Metadata metadata; // 이미지 메타 데이터 객체
        Directory directory; // 이미지 Exif 데이터를 읽기 위한 객체
        JpegDirectory jpegDirectory; // JPG 이미지 정보를 읽기 위한 객체

        try {
            metadata = ImageMetadataReader.readMetadata(imageFile);
            directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            jpegDirectory = metadata.getFirstDirectoryOfType(JpegDirectory.class);
            if (directory != null) {
                orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
            }

        } catch (Exception e) {
            orientation = 1;
        }

        BufferedImage srcImg = ImageIO.read(imageFile);
        AffineTransformOp[] xform = null;

        switch (orientation) {
            case 6:
                srcImg = Scalr.rotate(srcImg, Scalr.Rotation.CW_90, xform);
                break;
            case 1:
                break;
            case 3:
                srcImg = Scalr.rotate(srcImg, Scalr.Rotation.CW_180, xform);
                break;
            case 8:
                srcImg = Scalr.rotate(srcImg, Scalr.Rotation.CW_270, xform);
                break;
            default:
                orientation = 1;
                break;
        }
        double origin_w = srcImg.getWidth();
        double origin_h = srcImg.getHeight();

        if (origin_w > MAX) {
            double ratio = (MAX / origin_w);
            int thumb_w = (int) (origin_w * ratio);
            int thumb_h = (int) (origin_h * ratio);

            BufferedImage destImg = Scalr.resize(srcImg, thumb_w, thumb_h);
            String thumbFileNm = "t_" + fileName;
            File thumbFile = new File(basePath + "/" + thumbFileNm);
            ImageIO.write(destImg, extension, thumbFile);
        } else {
            BufferedImage destImg = Scalr.resize(srcImg, (int)origin_w, (int)origin_h);
            String thumbFileNm = "t_" + fileName;
            File thumbFile = new File(basePath + "/" + thumbFileNm);
            ImageIO.write(destImg, extension, thumbFile);
        }
    }
}
