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
@ApiModel(description = "照片信息")
public class PhotoInfoVO {

    @NotNull(message = "照片id不能为空")
    @ApiModelProperty(name = "id", value = "照片id", required = true, dataType = "Integer")
    private Integer id;

    @NotBlank(message = "照片名不能为空")
    @ApiModelProperty(name = "photoName", value = "照片名", required = true, dataType = "String")
    private String photoName;

    @ApiModelProperty(name = "photoDesc", value = "照片描述", dataType = "String")
    private String photoDesc;

}
