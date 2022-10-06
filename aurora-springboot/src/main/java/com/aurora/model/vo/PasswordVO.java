package com.aurora.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "密码")
public class PasswordVO {

    @NotBlank(message = "旧密码不能为空")
    @ApiModelProperty(name = "oldPassword", value = "旧密码", required = true, dataType = "String")
    private String oldPassword;

    @Size(min = 6, message = "新密码不能少于6位")
    @NotBlank(message = "新密码不能为空")
    @ApiModelProperty(name = "newPassword", value = "新密码", required = true, dataType = "String")
    private String newPassword;
}
