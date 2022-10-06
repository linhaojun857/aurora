package com.aurora.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "qq登录信息")
public class QQLoginVO {

    @NotBlank(message = "openId不能为空")
    @ApiModelProperty(name = "openId", value = "qq openId", required = true, dataType = "String")
    private String openId;

    @NotBlank(message = "accessToken不能为空")
    @ApiModelProperty(name = "accessToken", value = "qq accessToken", required = true, dataType = "String")
    private String accessToken;

}
