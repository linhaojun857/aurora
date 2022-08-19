package com.aurora.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author 花未眠
 * 用户禁用状态
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "用户禁用状态")
public class UserDisableVO {

    /**
     * id
     */
    @NotNull(message = "用户id不能为空")
    private Integer id;

    /**
     * 置顶状态
     */
    @NotNull(message = "用户禁用状态不能为空")
    private Integer isDisable;

}
