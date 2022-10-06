package com.aurora.model.dto;


import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.List;


/**
 * @author 花未眠
 * 文章编辑页码DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "文章")
public class ArticleAdminViewDTO {

    private Integer id;

    private String articleTitle;

    private String articleContent;

    private String articleCover;

    private String categoryName;

    private List<String> tagNames;

    private Integer isTop;

    private Integer isFeatured;

    private Integer status;

    private Integer type;

    private String originalUrl;

    private String password;

}
