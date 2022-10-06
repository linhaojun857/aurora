package com.aurora.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "标签对象")
public class TagVO {

    @ApiModelProperty(name = "id", value = "标签id", dataType = "Integer")
    private Integer id;

    @NotBlank(message = "标签名不能为空")
    @ApiModelProperty(name = "categoryName", value = "标签名", required = true, dataType = "String")
    private String tagName;

}
