package com.aurora.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 花未眠
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobStatusVO {

    @ApiModelProperty(name="任务id" ,value = "id",required = true, dataType = "Integer")
    private Integer id;

    @ApiModelProperty(name = "任务状态", value = "status", required = true, dataType = "Integer")
    private Integer status;
}
