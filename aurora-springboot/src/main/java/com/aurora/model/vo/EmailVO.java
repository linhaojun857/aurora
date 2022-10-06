package com.aurora.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "绑定邮箱")
public class EmailVO {

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @ApiModelProperty(name = "email", value = "用户名", required = true, dataType = "String")
    private String email;

    @NotBlank(message = "验证码不能为空")
    @ApiModelProperty(name = "code", value = "邮箱验证码", required = true, dataType = "String")
    private String code;

}
