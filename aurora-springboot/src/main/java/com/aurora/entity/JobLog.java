package com.aurora.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 花未眠
 * 定时任务调度日志
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_job_log")
public class JobLog{

    @ApiModelProperty(value = "任务日志ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "任务ID")
    private Integer jobId;

    @ApiModelProperty(value = "任务名称")
    private String jobName;

    @ApiModelProperty(value = "任务组名")
    private String jobGroup;

    @ApiModelProperty(value = "调用目标字符串")
    private String invokeTarget;

    @ApiModelProperty(value = "日志信息")
    private String jobMessage;

    @ApiModelProperty(value = "执行状态（0正常 1失败）")
    private Integer status;

    @ApiModelProperty(value = "异常信息")
    private String exceptionInfo;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 开始时间 */
    private Date startTime;
    /** 停止时间 */
    private Date endTime;
}
