package com.aurora.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "照片")
public class PhotoVO {

    @NotNull(message = "相册id不能为空")
    @ApiModelProperty(name = "id", value = "相册id", required = true, dataType = "Integer")
    private Integer albumId;

    @ApiModelProperty(name = "photoUrlList", value = "照片列表", required = true, dataType = "List<String>")
    private List<String> photoUrls;

    @ApiModelProperty(name = "photoIdList", value = "照片id列表", required = true, dataType = "List<Integer>")
    private List<Integer> photoIds;

}
