package com.aurora.utils;

import com.aurora.dto.ArticleCardDTO;

import java.util.List;
import java.util.Objects;

public class MarkdownUtils {

    public static String deleteMarkdownKeyword(String str) {
        return str.replaceAll("[\\\\\\`\\*\\_\\[\\]\\#\\+\\-\\!\\>]", "");
    }

    public static void CutOutArticleContent(ArticleCardDTO articleCardDTO, Integer size) {
        if (Objects.nonNull(articleCardDTO)) {
            if (articleCardDTO.getArticleContent().length() > size) {
                articleCardDTO.setArticleContent(MarkdownUtils.deleteMarkdownKeyword(articleCardDTO.getArticleContent().substring(0, size)) + "...");
            }
        }
    }

    public static void CutOutArticleContent(List<ArticleCardDTO> articleCardDTOS, Integer size) {
        articleCardDTOS.forEach((item) -> {
            if (item.getArticleContent().length() > size) {
                item.setArticleContent(MarkdownUtils.deleteMarkdownKeyword(item.getArticleContent().substring(0, size)) + "...");
            }
        });
    }

}
