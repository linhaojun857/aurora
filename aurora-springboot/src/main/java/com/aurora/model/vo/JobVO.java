package com.aurora.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobVO {

    @ApiModelProperty(value = "任务id")
    private Integer id;

    @ApiModelProperty(value = "任务名称")
    private String jobName;

    @ApiModelProperty(value = "任务组名")
    private String jobGroup;

    @ApiModelProperty(value = "调用目标字符串")
    private String invokeTarget;

    @ApiModelProperty(value = "cron执行表达式")
    private String cronExpression;

    @ApiModelProperty(value = "计划执行错误策略（1立即执行 2执行一次 3放弃执行）")
    private Integer misfirePolicy;

    @ApiModelProperty(value = "是否并发执行（0允许 1禁止）")
    private Integer concurrent;

    @ApiModelProperty(value = "状态（0正常 1暂停）")
    private Integer status;

    @ApiModelProperty(value = "备注信息")
    private String remark;
}
