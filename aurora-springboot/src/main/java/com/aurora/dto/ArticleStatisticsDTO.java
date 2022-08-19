package com.aurora.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 花未眠
 * 文章统计
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleStatisticsDTO {

    /**
     * 日期
     */
    private String date;

    /**
     * 数量
     */
    private Integer count;

}
