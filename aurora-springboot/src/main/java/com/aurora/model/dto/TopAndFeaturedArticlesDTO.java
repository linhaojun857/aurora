package com.aurora.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 花未眠
 * 首页置顶与推荐文章的DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopAndFeaturedArticlesDTO {
    /**
     * 置顶文章
     */
    private ArticleCardDTO topArticle;
    /**
     * 推荐文章
     */
    private List<ArticleCardDTO> featuredArticles;
}
