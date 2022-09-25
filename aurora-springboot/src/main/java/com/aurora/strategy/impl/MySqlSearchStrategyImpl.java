package com.aurora.strategy.impl;

import com.aurora.entity.Article;
import com.aurora.mapper.ArticleMapper;
import com.aurora.model.dto.ArticleSearchDTO;
import com.aurora.strategy.SearchStrategy;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.aurora.constant.CommonConst.*;
import static com.aurora.enums.ArticleStatusEnum.PUBLIC;


/**
 * @author 花未眠
 * mysql搜索策略
 */
@Service("mySqlSearchStrategyImpl")
public class MySqlSearchStrategyImpl implements SearchStrategy {

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public List<ArticleSearchDTO> searchArticle(String keywords) {
        // 判空
        if (StringUtils.isBlank(keywords)) {
            return new ArrayList<>();
        }
        // 搜索文章
        List<Article> articles = articleMapper.selectList(new LambdaQueryWrapper<Article>()
                .eq(Article::getIsDelete, FALSE)
                .eq(Article::getStatus, PUBLIC.getStatus())
                .and(i -> i.like(Article::getArticleTitle, keywords)
                        .or()
                        .like(Article::getArticleContent, keywords)));
        // 高亮处理
        return articles.stream().map(item -> {
                    // 文章内容高亮
                    boolean isLowerCase = true;
                    String articleContent = item.getArticleContent();
                    int contentIndex = item.getArticleContent().indexOf(keywords.toLowerCase());
                    if (contentIndex == -1) {
                        contentIndex = item.getArticleContent().indexOf(keywords.toUpperCase());
                        if (contentIndex != -1) {
                            isLowerCase = false;
                        }
                    }
                    if (contentIndex != -1) {
                        // 获取关键词前面的文字
                        int preIndex = contentIndex > 15 ? contentIndex - 15 : 0;
                        String preText = item.getArticleContent().substring(preIndex, contentIndex);
                        // 获取关键词到后面的文字
                        int last = contentIndex + keywords.length();
                        int postLength = item.getArticleContent().length() - last;
                        int postIndex = postLength > 35 ? last + 35 : last + postLength;
                        String postText = item.getArticleContent().substring(contentIndex, postIndex);
                        // 文章内容高亮
                        if (isLowerCase) {
                            articleContent = (preText + postText).replaceAll(keywords.toLowerCase(), PRE_TAG + keywords.toLowerCase() + POST_TAG);
                        } else {
                            articleContent = (preText + postText).replaceAll(keywords.toUpperCase(), PRE_TAG + keywords.toUpperCase() + POST_TAG);
                        }
                    } else {
                        return null;
                    }
                    isLowerCase = true;
                    int titleIndex = item.getArticleTitle().indexOf(keywords.toLowerCase());
                    if (titleIndex == -1) {
                        titleIndex = item.getArticleTitle().indexOf(keywords.toUpperCase());
                        if (titleIndex != -1) {
                            isLowerCase = false;
                        }
                    }
                    // 文章标题高亮
                    String articleTitle;
                    if (isLowerCase) {
                        articleTitle = item.getArticleTitle().replaceAll(keywords.toLowerCase(), PRE_TAG + keywords.toLowerCase() + POST_TAG);
                    } else {
                        articleTitle = item.getArticleTitle().replaceAll(keywords.toUpperCase(), PRE_TAG + keywords.toUpperCase() + POST_TAG);
                    }
                    return ArticleSearchDTO.builder()
                            .id(item.getId())
                            .articleTitle(articleTitle)
                            .articleContent(articleContent)
                            .build();
                }).filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}
