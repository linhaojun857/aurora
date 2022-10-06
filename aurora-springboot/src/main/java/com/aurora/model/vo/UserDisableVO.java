package com.aurora.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "用户禁用状态")
public class UserDisableVO {

    @NotNull(message = "用户id不能为空")
    private Integer id;

    @NotNull(message = "用户禁用状态不能为空")
    private Integer isDisable;

}
