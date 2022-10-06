package com.aurora.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_exception_log")
public class ExceptionLog {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String optUri;

    private String optMethod;

    private String requestMethod;

    private String requestParam;

    private String optDesc;

    private String exceptionInfo;

    private String ipAddress;

    private String ipSource;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}
