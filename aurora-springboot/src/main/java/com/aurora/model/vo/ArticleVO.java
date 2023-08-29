package com.aurora.model.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "文章")
public class ArticleVO {

    @ApiModelProperty(name = "id", value = "文章id", dataType = "Integer")
    private Integer id;

    @NotBlank(message = "文章标题不能为空")
    @ApiModelProperty(name = "articleTitle", value = "文章标题", required = true, dataType = "String")
    private String articleTitle;

    @NotBlank(message = "文章内容不能为空")
    @ApiModelProperty(name = "articleContent", value = "文章内容", required = true, dataType = "String")
    private String articleContent;

    @ApiModelProperty(name = "articleAbstract", value = "文章摘要", dataType = "String")
    private String articleAbstract;

    @ApiModelProperty(name = "articleCover", value = "文章缩略图", dataType = "String")
    private String articleCover;

    @ApiModelProperty(name = "category", value = "文章分类", dataType = "Integer")
    private String categoryName;

    @ApiModelProperty(name = "tagNameList", value = "文章标签", dataType = "List<Integer>")
    private List<String> tagNames;

    @ApiModelProperty(name = "isTop", value = "是否置顶", dataType = "Integer")
    private Integer isTop;

    @ApiModelProperty(name = "isFeatured", value = "是否推荐", dataType = "Integer")
    private Integer isFeatured;

    @ApiModelProperty(name = "status", value = "文章状态", dataType = "String")
    private Integer status;

    @ApiModelProperty(name = "type", value = "文章类型", dataType = "Integer")
    private Integer type;

    @ApiModelProperty(name = "originalUrl", value = "原文链接", dataType = "String")
    private String originalUrl;

    @ApiModelProperty(name = "password", value = "文章访问密码", dataType = "String")
    private String password;
}
