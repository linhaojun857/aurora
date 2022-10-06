package com.aurora.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "用户账号")
public class UserVO {

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @ApiModelProperty(name = "username", value = "用户名", required = true, dataType = "String")
    private String username;

    @Size(min = 6, message = "密码不能少于6位")
    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(name = "password", value = "密码", required = true, dataType = "String")
    private String password;

    @NotBlank(message = "验证码不能为空")
    @ApiModelProperty(name = "code", value = "邮箱验证码", required = true, dataType = "String")
    private String code;

}
