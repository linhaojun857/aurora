package com.aurora.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobLogDTO {
    private Integer id;
    private Integer jobId;
    private String jobName;
    private String jobGroup;
    private String invokeTarget;
    private String jobMessage;
    private Integer status;
    private String exceptionInfo;
    private LocalDateTime createTime;
    private Date startTime;
    private Date endTime;
}
