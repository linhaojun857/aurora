package com.aurora.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author 花未眠
 * 用户角色VO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "用户权限")
public class UserRoleVO {
    /**
     * 用户id
     */
    @NotNull(message = "id不能为空")
    @ApiModelProperty(name = "userInfoId", value = "用户信息id", dataType = "Integer")
    private Integer userInfoId;

    /**
     * 用户昵称
     */
    @NotBlank(message = "昵称不能为空")
    @ApiModelProperty(name = "nickname", value = "昵称", dataType = "String")
    private String nickname;

    /**
     * 用户角色
     */
    @NotNull(message = "用户角色不能为空")
    @ApiModelProperty(name = "roleList", value = "角色id集合", dataType = "List<Integer>")
    private List<Integer> roleIds;

}
