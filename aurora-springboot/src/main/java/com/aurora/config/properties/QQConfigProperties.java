package com.aurora.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "qq")
public class QQConfigProperties {

    private String appId;

    private String checkTokenUrl;

    private String userInfoUrl;

}
