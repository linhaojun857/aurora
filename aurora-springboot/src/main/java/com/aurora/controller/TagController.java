package com.aurora.controller;


import com.aurora.annotation.OptLog;
import com.aurora.model.dto.TagAdminDTO;
import com.aurora.model.dto.TagDTO;
import com.aurora.model.vo.ResultVO;
import com.aurora.service.TagService;
import com.aurora.model.vo.ConditionVO;
import com.aurora.model.dto.PageResultDTO;
import com.aurora.model.vo.TagVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.aurora.constant.OptTypeConstant.*;

@Api(tags = "标签模块")
@RestController
public class TagController {


    @Autowired
    private TagService tagService;

    @ApiOperation("获取所有标签")
    @GetMapping("/tags/all")
    public ResultVO<List<TagDTO>> getAllTags() {
        return ResultVO.ok(tagService.listTags());
    }

    @ApiOperation("获取前十个标签")
    @GetMapping("/tags/topTen")
    public ResultVO<List<TagDTO>> getTopTenTags() {
        return ResultVO.ok(tagService.listTopTenTags());
    }

    @ApiOperation(value = "查询后台标签列表")
    @GetMapping("/admin/tags")
    public ResultVO<PageResultDTO<TagAdminDTO>> listTagsAdmin(ConditionVO conditionVO) {
        return ResultVO.ok(tagService.listTagsAdmin(conditionVO));
    }

    @ApiOperation(value = "搜索文章标签")
    @GetMapping("/admin/tags/search")
    public ResultVO<List<TagAdminDTO>> listTagsAdminBySearch(ConditionVO condition) {
        return ResultVO.ok(tagService.listTagsAdminBySearch(condition));
    }

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改标签")
    @PostMapping("/admin/tags")
    public ResultVO<?> saveOrUpdateTag(@Valid @RequestBody TagVO tagVO) {
        tagService.saveOrUpdateTag(tagVO);
        return ResultVO.ok();
    }

    @OptLog(optType = DELETE)
    @ApiOperation(value = "删除标签")
    @DeleteMapping("/admin/tags")
    public ResultVO<?> deleteTag(@RequestBody List<Integer> tagIdList) {
        tagService.deleteTag(tagIdList);
        return ResultVO.ok();
    }
}
