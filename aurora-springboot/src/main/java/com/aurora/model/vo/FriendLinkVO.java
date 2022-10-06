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
@ApiModel(description = "友链")
public class FriendLinkVO {

    @ApiModelProperty(name = "categoryId", value = "友链id", dataType = "Integer")
    private Integer id;

    @NotBlank(message = "链接名不能为空")
    @ApiModelProperty(name = "linkName", value = "友链名", dataType = "String", required = true)
    private String linkName;

    @NotBlank(message = "链接头像不能为空")
    @ApiModelProperty(name = "linkAvatar", value = "友链头像", dataType = "String", required = true)
    private String linkAvatar;

    @NotBlank(message = "链接地址不能为空")
    @ApiModelProperty(name = "linkAddress", value = "友链头像", dataType = "String", required = true)
    private String linkAddress;

    @NotBlank(message = "链接介绍不能为空")
    @ApiModelProperty(name = "linkIntro", value = "友链头像", dataType = "String", required = true)
    private String linkIntro;

}
