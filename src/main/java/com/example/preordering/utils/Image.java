package com.example.preordering.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Image {
    public static final String COMPANY_IMAGE = "company";
    public static final String CLIENT_IMAGE = "client";
    public static final String USERADMIN_IMAGE = "useradmin";
    public static final String CATEGORY_IMAGE = "category";
    public static void saveImage(MultipartFile multipartFile, String objectType, String username){
        try {
            File file = new ClassPathResource("static/img/" + objectType).getFile();
            Path uploadDir = Paths.get(file.getAbsolutePath() + File.separator +
                    username + "-" + multipartFile.getOriginalFilename());
            Files.copy(multipartFile.getInputStream(), uploadDir, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
