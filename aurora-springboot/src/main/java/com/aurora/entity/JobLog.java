package com.aurora.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_job_log")
public class JobLog {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer jobId;

    private String jobName;

    private String jobGroup;

    private String invokeTarget;

    private String jobMessage;

    private Integer status;

    private String exceptionInfo;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    private Date startTime;

    private Date endTime;
}
