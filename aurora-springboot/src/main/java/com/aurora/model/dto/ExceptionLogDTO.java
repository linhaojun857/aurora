package com.aurora.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionLogDTO {

    private Integer id;

    private String optUri;

    private String optMethod;

    private String requestMethod;

    private String requestParam;

    private String optDesc;

    private String exceptionInfo;

    private String ipAddress;

    private String ipSource;

    private LocalDateTime createTime;

}
