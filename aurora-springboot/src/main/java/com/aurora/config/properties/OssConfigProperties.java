package com.aurora.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "upload.oss")
public class OssConfigProperties {

    private String url;

    private String endpoint;

    private String accessKeyId;

    private String accessKeySecret;

    private String bucketName;

}
