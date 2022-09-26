package com.aurora.controller;

import com.aurora.annotation.OptLog;
import com.aurora.model.dto.*;
import com.aurora.enums.FilePathEnum;
import com.aurora.service.ArticleService;
import com.aurora.strategy.context.ArticleImportStrategyContext;
import com.aurora.strategy.context.UploadStrategyContext;
import com.aurora.model.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import java.util.List;

import static com.aurora.constant.OptTypeConstant.*;

@Api(tags = "文章模块")
@RestController
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UploadStrategyContext uploadStrategyContext;

    @Autowired
    private ArticleImportStrategyContext articleImportStrategyContext;

    @ApiOperation("获取置顶和推荐文章")
    @GetMapping("/articles/topAndFeatured")
    public Result<TopAndFeaturedArticlesDTO> listTopAndFeaturedArticles() {
        return Result.ok(articleService.listTopAndFeaturedArticles());
    }

    @ApiOperation("获取所有文章")
    @GetMapping("/articles/all")
    public Result<PageResult<ArticleCardDTO>> listArticles() {
        return Result.ok(articleService.listArticles());
    }

    @ApiOperation("根据分类id获取文章")
    @GetMapping("/articles/categoryId")
    public Result<PageResult<ArticleCardDTO>> getArticlesByCategoryId(@RequestParam Integer categoryId) {
        return Result.ok(articleService.listArticlesByCategoryId(categoryId));
    }

    @ApiOperation("根据id获取文章")
    @GetMapping("/articles/{id}")
    public Result<ArticleDTO> getArticleById(@PathVariable("id") Integer articleId) {
        return Result.ok(articleService.getArticleById(articleId));
    }

    @ApiOperation("校验文章访问密码")
    @PostMapping("/articles/access")
    public Result<?> accessArticle(@Valid @RequestBody ArticlePasswordVO articlePasswordVO) {
        articleService.accessArticle(articlePasswordVO);
        return Result.ok();
    }

    @ApiOperation("根据标签id获取文章")
    @GetMapping("/articles/tagId")
    public Result<PageResult<ArticleCardDTO>> listArticlesByTagId(@RequestParam Integer tagId) {
        return Result.ok(articleService.listArticlesByTagId(tagId));
    }

    @ApiOperation("获取所有文章归档")
    @GetMapping("/archives/all")
    public Result<PageResult<ArchiveDTO>> listArchives() {
        return Result.ok(articleService.listArchives());
    }

    @ApiOperation("获取后台文章")
    @GetMapping("/admin/articles")
    public Result<PageResult<ArticleAdminDTO>> listArticlesAdmin(ConditionVO conditionVO) {
        return Result.ok(articleService.listArticlesAdmin(conditionVO));
    }

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation("保存和修改文章")
    @PostMapping("/admin/articles")
    public Result<?> saveOrUpdateArticle(@Valid @RequestBody ArticleVO articleVO) {
        articleService.saveOrUpdateArticle(articleVO);
        return Result.ok();
    }

    @OptLog(optType = UPDATE)
    @ApiOperation("修改文章是否置顶和推荐")
    @PutMapping("/admin/articles/topAndFeatured")
    public Result<?> updateArticleTopAndFeatured(@Valid @RequestBody ArticleTopFeaturedVO articleTopFeaturedVO) {
        articleService.updateArticleTopAndFeatured(articleTopFeaturedVO);
        return Result.ok();
    }

    @ApiOperation("删除或者恢复文章")
    @PutMapping("/admin/articles")
    public Result<?> updateArticleDelete(@Valid @RequestBody DeleteVO deleteVO) {
        articleService.updateArticleDelete(deleteVO);
        return Result.ok();
    }

    @OptLog(optType = DELETE)
    @ApiOperation(value = "物理删除文章")
    @DeleteMapping("/admin/articles/delete")
    public Result<?> deleteArticles(@RequestBody List<Integer> articleIds) {
        articleService.deleteArticles(articleIds);
        return Result.ok();
    }

    @OptLog(optType = UPLOAD)
    @ApiOperation("上传文章图片")
    @ApiImplicitParam(name = "file", value = "文章图片", required = true, dataType = "MultipartFile")
    @PostMapping("/admin/articles/images")
    public Result<String> saveArticleImages(MultipartFile file) {
        return Result.ok(uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.ARTICLE.getPath()));
    }

    @ApiOperation("根据id查看后台文章")
    @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "Integer")
    @GetMapping("/admin/articles/{articleId}")
    public Result<ArticleAdminViewDTO> getArticleBackById(@PathVariable("articleId") Integer articleId) {
        return Result.ok(articleService.getArticleByIdAdmin(articleId));
    }

    @OptLog(optType = UPLOAD)
    @ApiOperation(value = "导入文章")
    @PostMapping("/admin/articles/import")
    public Result<?> importArticles(MultipartFile file, @RequestParam(required = false) String type) {
        articleImportStrategyContext.importArticles(file, type);
        return Result.ok();
    }

    @OptLog(optType = EXPORT)
    @ApiOperation(value = "导出文章")
    @ApiImplicitParam(name = "articleIdList", value = "文章id", required = true, dataType = "List<Integer>")
    @PostMapping("/admin/articles/export")
    public Result<List<String>> exportArticles(@RequestBody List<Integer> articleIds) {
        return Result.ok(articleService.exportArticles(articleIds));
    }

    @ApiOperation(value = "搜索文章")
    @GetMapping("/articles/search")
    public Result<List<ArticleSearchDTO>> listArticlesBySearch(ConditionVO condition) {
        return Result.ok(articleService.listArticlesBySearch(condition));
    }

}
