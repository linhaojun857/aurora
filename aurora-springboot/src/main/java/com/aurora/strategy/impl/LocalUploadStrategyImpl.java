package com.aurora.strategy.impl;

import com.aurora.config.properties.LocalProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service("localUploadStrategyImpl")
public class LocalUploadStrategyImpl extends AbstractUploadStrategyImpl {

    @Value("${upload.local.baseUrl}")
    private String baseUrl;

    @Autowired
    private LocalProperties localProperties;
    private String imagePath;

    @PostConstruct
    public void init() {
        imagePath = localProperties.getImagePath();
        // 确保存储目录存在
        new File(imagePath).mkdirs();
    }

    @Override
    public Boolean exists(String filePath) {
        // 检查文件是否存在
        return new File(imagePath + filePath).exists();
    }

    @Override
    public void upload(String path, String fileName, InputStream inputStream) throws IOException {
        // 重命名文件，添加自定义前缀
//        String customFileName = "aurora_" + fileName;
        String customFileName =  fileName;
        // 确保上传目录存在
        File directory = new File(imagePath + path);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 写入文件
        File file = new File(directory, customFileName);
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }

@Override
public String getFileAccessUrl(String filePath) {
    // 假设您的图片通过HTTP服务器在以下URL下可访问
    return baseUrl + filePath;
}
}
