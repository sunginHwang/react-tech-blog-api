package com.woolta.blog.util;

import com.woolta.blog.domain.response.UploadFileResponse;
import com.woolta.blog.exception.ImageUploadException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class ImageUtil {

    private final String uploadPath = "/home/blog/post/upload/";
    private final String url = "";

    public UploadFileResponse uploadImage(MultipartFile file) {



        if (file == null) {
            throw new ImageUploadException("[IMAGE_UTIL] file not exist");
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String ext = getFileExtension(fileName);
        String uploadFileName = Long.toHexString(Double.doubleToLongBits(Math.random()))+ "." + ext;

        if (!isImageFile(ext)) {
            throw new ImageUploadException("[IMAGE_UTIL] " + fileName + " ext : "+ext+" is not image format ext:");
        }

        if (fileName.contains("..")) {
            throw new ImageUploadException("[IMAGE_UTIL] invalid file path imageName: " + fileName);
        }

        try {

            File imageFile = new File(uploadPath, uploadFileName);
            file.transferTo(imageFile);

            UploadFileResponse uploadFileResponse;
            uploadFileResponse = UploadFileResponse.builder()
                    .originFileName(uploadFileName)
                    .fileName(fileName)
                    .fileDir(uploadPath)
                    .fileExt(ext)
                    .fullPath(url+uploadPath+uploadFileName)
                    .size(file.getSize())
                    .build();

            return uploadFileResponse;
        } catch (IOException ex) {
            throw new ImageUploadException("[IMAGE_UTIL] fail to image upload imageName:" + fileName + " error :" + ex);
        }
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.')+1).toLowerCase();
    }

    private boolean isImageFile(String ext) {

        String allowPattern = "(png|jpg|bmp|jpeg)";
        Pattern p = Pattern.compile(allowPattern);
        Matcher m = p.matcher(ext);

        return m.matches();
    }

}
