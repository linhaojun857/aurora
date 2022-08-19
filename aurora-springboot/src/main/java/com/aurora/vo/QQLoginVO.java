package com.aurora.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author 花未眠
 * QQ登录
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "qq登录信息")
public class QQLoginVO {

    /**
     * openId
     */
    @NotBlank(message = "openId不能为空")
    @ApiModelProperty(name = "openId", value = "qq openId", required = true, dataType = "String")
    private String openId;

    /**
     * accessToken
     */
    @NotBlank(message = "accessToken不能为空")
    @ApiModelProperty(name = "accessToken", value = "qq accessToken", required = true, dataType = "String")
    private String accessToken;

}
