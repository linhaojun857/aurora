package com.aurora.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import javax.validation.constraints.NotBlank;
import java.util.List;


/**
 * @author 花未眠
 * 文章
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "文章")
public class ArticleVO {

    /**
     * 文章id
     */
    @ApiModelProperty(name = "id", value = "文章id", dataType = "Integer")
    private Integer id;

    /**
     * 标题
     */
    @NotBlank(message = "文章标题不能为空")
    @ApiModelProperty(name = "articleTitle", value = "文章标题", required = true, dataType = "String")
    private String articleTitle;

    /**
     * 内容
     */
    @NotBlank(message = "文章内容不能为空")
    @ApiModelProperty(name = "articleContent", value = "文章内容", required = true, dataType = "String")
    private String articleContent;

    /**
     * 文章封面
     */
    @ApiModelProperty(name = "articleCover", value = "文章缩略图", dataType = "String")
    private String articleCover;

    /**
     * 文章分类
     */
    @ApiModelProperty(name = "category", value = "文章分类", dataType = "Integer")
    private String categoryName;

    /**
     * 文章标签
     */
    @ApiModelProperty(name = "tagNameList", value = "文章标签", dataType = "List<Integer>")
    private List<String> tagNames;

    /**
     * 是否置顶
     */
    @ApiModelProperty(name = "isTop", value = "是否置顶", dataType = "Integer")
    private Integer isTop;

    /**
     * 是否推荐
     */
    @ApiModelProperty(name = "isFeatured", value = "是否推荐", dataType = "Integer")
    private Integer isFeatured;

    /**
     * 文章状态 1.公开 2.私密
     */
    @ApiModelProperty(name = "status", value = "文章状态", dataType = "String")
    private Integer status;

    /**
     * 文章类型
     */
    @ApiModelProperty(name = "type", value = "文章类型", dataType = "Integer")
    private Integer type;

    /**
     * 原文链接
     */
    @ApiModelProperty(name = "originalUrl", value = "原文链接", dataType = "String")
    private String originalUrl;
}
