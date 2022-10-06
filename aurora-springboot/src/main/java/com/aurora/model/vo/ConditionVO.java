package com.aurora.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "查询条件")
public class ConditionVO {

    @ApiModelProperty(name = "current", value = "页码", dataType = "Long")
    private Long current;

    @ApiModelProperty(name = "size", value = "条数", dataType = "Long")
    private Long size;

    @ApiModelProperty(name = "keywords", value = "搜索内容", dataType = "String")
    private String keywords;

    @ApiModelProperty(name = "categoryId", value = "分类id", dataType = "Integer")
    private Integer categoryId;

    @ApiModelProperty(name = "tagId", value = "标签id", dataType = "Integer")
    private Integer tagId;

    @ApiModelProperty(name = "albumId", value = "相册id", dataType = "Integer")
    private Integer albumId;

    @ApiModelProperty(name = "loginType", value = "登录类型", dataType = "Integer")
    private Integer loginType;

    @ApiModelProperty(name = "type", value = "类型", dataType = "Integer")
    private Integer type;

    @ApiModelProperty(name = "status", value = "状态", dataType = "Integer")
    private Integer status;

    @ApiModelProperty(name = "startTime", value = "开始时间", dataType = "LocalDateTime")
    private LocalDateTime startTime;

    @ApiModelProperty(name = "endTime", value = "结束时间", dataType = "LocalDateTime")
    private LocalDateTime endTime;

    @ApiModelProperty(name = "isDelete", value = "是否删除", dataType = "Integer")
    private Integer isDelete;

    @ApiModelProperty(name = "isReview", value = "是否审核", dataType = "Integer")
    private Integer isReview;

    @ApiModelProperty(name = "isTop", value = "是否置顶", dataType = "Integer")
    private Integer isTop;

    @ApiModelProperty(name = "isFeatured", value = "是否推荐", dataType = "Integer")
    private Integer isFeatured;


}
