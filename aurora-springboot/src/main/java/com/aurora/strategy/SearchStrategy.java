package com.aurora.strategy;

import com.aurora.dto.ArticleSearchDTO;

import java.util.List;

/**
 * @author 花未眠
 * 搜索策略
 */
public interface SearchStrategy {

    List<ArticleSearchDTO> searchArticle(String keywords);

}
