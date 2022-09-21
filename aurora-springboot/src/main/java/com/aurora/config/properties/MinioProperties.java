package com.aurora.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "upload.minio")
public class MinioProperties {
    /**
     * minio域名
     */
    private String url;
    /**
     * 终点
     */
    private String endpoint;
    /**
     * accessKey
     */
    private String accessKey;
    /**
     * secretKey
     */
    private String secretKey;
    /**
     * 桶名称
     */
    private String bucketName;
}
