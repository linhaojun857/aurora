package com.aurora.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author 花未眠
 * 文章排行
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleRankDTO {

    /**
     * 标题
     */
    private String articleTitle;

    /**
     * 浏览量
     */
    private Integer viewsCount;


}
