package com.aurora.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "upload.minio")
public class MinioProperties {

    private String url;

    private String endpoint;

    private String accessKey;

    private String secretKey;

    private String bucketName;
}
