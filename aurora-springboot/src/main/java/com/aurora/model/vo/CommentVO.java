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
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "评论")
public class CommentVO {

    @ApiModelProperty(name = "replyUserId", value = "回复用户id", dataType = "Integer")
    private Integer replyUserId;

    @ApiModelProperty(name = "topicId", value = "主题id", dataType = "Integer")
    private Integer topicId;

    @NotBlank(message = "评论内容不能为空")
    @ApiModelProperty(name = "commentContent", value = "评论内容", required = true, dataType = "String")
    private String commentContent;

    @ApiModelProperty(name = "parentId", value = "评论父id", dataType = "Integer")
    private Integer parentId;

    @NotNull(message = "评论类型不能为空")
    @ApiModelProperty(name = "type", value = "评论类型", dataType = "Integer")
    private Integer type;
}
