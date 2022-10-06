package com.aurora.strategy;

import com.aurora.model.dto.ArticleSearchDTO;

import java.util.List;

public interface SearchStrategy {

    List<ArticleSearchDTO> searchArticle(String keywords);

}
