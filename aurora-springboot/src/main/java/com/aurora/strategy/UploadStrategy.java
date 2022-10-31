package com.aurora.strategy;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface UploadStrategy {

    String uploadFile(MultipartFile file, String path);

    String uploadFile(String fileName, InputStream inputStream, String path);

}
