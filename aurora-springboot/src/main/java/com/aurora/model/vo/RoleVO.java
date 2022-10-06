package com.aurora.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "角色")
public class RoleVO {

    @ApiModelProperty(name = "id", value = "用户id", dataType = "Integer")
    private Integer id;

    @NotBlank(message = "角色名不能为空")
    @ApiModelProperty(name = "roleName", value = "角色名", required = true, dataType = "String")
    private String roleName;

    @ApiModelProperty(name = "resourceIdList", value = "资源列表", required = true, dataType = "List<Integer>")
    private List<Integer> resourceIds;

    @ApiModelProperty(name = "menuIdList", value = "菜单列表", required = true, dataType = "List<Integer>")
    private List<Integer> menuIds;

}
