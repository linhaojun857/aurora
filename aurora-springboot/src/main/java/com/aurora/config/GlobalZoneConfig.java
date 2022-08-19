package com.aurora.config;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

import static com.aurora.enums.ZoneEnum.SHANGHAI;

@Configuration
public class GlobalZoneConfig {

    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone(SHANGHAI.getZone()));
    }
}
