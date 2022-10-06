package com.aurora.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "菜单")
public class MenuVO {

    @ApiModelProperty(name = "id", value = "菜单id", dataType = "Integer")
    private Integer id;

    @NotBlank(message = "菜单名不能为空")
    @ApiModelProperty(name = "name", value = "菜单名", dataType = "String")
    private String name;

    @NotBlank(message = "菜单icon不能为空")
    @ApiModelProperty(name = "icon", value = "菜单icon", dataType = "String")
    private String icon;

    @NotBlank(message = "路径不能为空")
    @ApiModelProperty(name = "path", value = "路径", dataType = "String")
    private String path;

    @NotBlank(message = "组件不能为空")
    @ApiModelProperty(name = "component", value = "组件", dataType = "String")
    private String component;

    @NotNull(message = "排序不能为空")
    @ApiModelProperty(name = "orderNum", value = "排序", dataType = "Integer")
    private Integer orderNum;

    @ApiModelProperty(name = "parentId", value = "父id", dataType = "Integer")
    private Integer parentId;

    @ApiModelProperty(name = "isHidden", value = "是否隐藏", dataType = "Integer")
    private Integer isHidden;

}
