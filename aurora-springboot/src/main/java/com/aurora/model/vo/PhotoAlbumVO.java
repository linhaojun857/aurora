package com.aurora.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "相册")
public class PhotoAlbumVO {

    @ApiModelProperty(name = "id", value = "相册id", required = true, dataType = "Integer")
    private Integer id;

    @NotBlank(message = "相册名不能为空")
    @ApiModelProperty(name = "albumName", value = "相册名", required = true, dataType = "String")
    private String albumName;

    @ApiModelProperty(name = "albumDesc", value = "相册描述", dataType = "String")
    private String albumDesc;

    @NotBlank(message = "相册封面不能为空")
    @ApiModelProperty(name = "albumCover", value = "相册封面", required = true, dataType = "String")
    private String albumCover;

    @ApiModelProperty(name = "status", value = "状态值", required = true, dataType = "Integer")
    private Integer status;

}
