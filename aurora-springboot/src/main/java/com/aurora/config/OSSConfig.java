package com.aurora.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OSSConfig {

    @Autowired
    private OssConfigProperties ossConfigProperties;

    @Bean
    public OSS OSS() {
        return new OSSClientBuilder()
                .build(ossConfigProperties.getEndpoint(),
                        ossConfigProperties.getAccessKeyId(),
                        ossConfigProperties.getAccessKeySecret());
    }
}
