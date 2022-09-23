package com.aurora.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AboutVO {

    @ApiModelProperty(name = "About内容", value = "content", required = true, dataType = "String")
    private String content;
}
