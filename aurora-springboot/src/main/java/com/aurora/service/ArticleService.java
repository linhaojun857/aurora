package com.aurora.service;

import com.aurora.dto.*;
import com.aurora.entity.Article;
import com.aurora.vo.*;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface ArticleService extends IService<Article> {

    TopAndFeaturedArticlesDTO listTopAndFeaturedArticles();

    PageResult<ArticleCardDTO> listArticles();

    PageResult<ArticleCardDTO> listArticlesByCategoryId(Integer categoryId);

    ArticleDTO getArticleById(Integer articleId);

    PageResult<ArticleCardDTO> listArticlesByTagId(Integer tagId);

    PageResult<ArchiveDTO> listArchives();

    PageResult<ArticleAdminDTO> listArticlesAdmin(ConditionVO conditionVO);

    void saveOrUpdateArticle(ArticleVO articleVO);

    void updateArticleTopAndFeatured(ArticleTopFeaturedVO articleTopFeaturedVO);

    void updateArticleDelete(DeleteVO deleteVO);

    void deleteArticles(List<Integer> articleIds);

    ArticleAdminViewDTO getArticleByIdAdmin(Integer articleId);

    List<String> exportArticles(List<Integer> articleIdList);

    List<ArticleSearchDTO> listArticlesBySearch(ConditionVO condition);
}
