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
@ApiModel(description = "说说对象")
public class TalkVO {

    @ApiModelProperty(name = "id", value = "说说id", dataType = "Integer")
    private Integer id;

    @ApiModelProperty(name = "content", value = "说说内容", dataType = "String")
    @NotBlank(message = "说说内容不能为空")
    private String content;

    @ApiModelProperty(name = "images", value = "说说图片", dataType = "String")
    private String images;

    @ApiModelProperty(name = "isTop", value = "置顶状态", dataType = "Integer")
    @NotNull(message = "置顶状态不能为空")
    private Integer isTop;

    @ApiModelProperty(name = "status", value = "说说状态", dataType = "Integer")
    @NotNull(message = "说说状态不能为空")
    private Integer status;

}
