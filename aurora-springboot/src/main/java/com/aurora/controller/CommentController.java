package com.aurora.controller;

import com.aurora.annotation.OptLog;
import com.aurora.dto.CommentAdminDTO;
import com.aurora.dto.CommentDTO;
import com.aurora.service.CommentService;
import com.aurora.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.aurora.constant.OptTypeConst.*;

@Api(tags = "评论模块")
@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @OptLog(optType = SAVE)
    @ApiOperation("添加评论")
    @PostMapping("/comments/save")
    public Result<?> saveComment(@Valid @RequestBody CommentVO commentVO) {
        commentService.saveComment(commentVO);
        return Result.ok();
    }

    @ApiOperation("获取评论")
    @GetMapping("/comments")
    public Result<List<CommentDTO>> getComments(CommentVO commentVO) {
        return Result.ok(commentService.listComments(commentVO));
    }

    @ApiOperation("获取前七个评论")
    @GetMapping("/comments/topSeven")
    public Result<List<CommentDTO>> getTopSevenComments() {
        return Result.ok(commentService.listTopSevenComments());
    }

    @ApiOperation(value = "查询后台评论")
    @GetMapping("/admin/comments")
    public Result<PageResult<CommentAdminDTO>> listCommentBackDTO(ConditionVO conditionVO) {
        return Result.ok(commentService.listCommentsAdmin(conditionVO));
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "审核评论")
    @PutMapping("/admin/comments/review")
    public Result<?> updateCommentsReview(@Valid @RequestBody ReviewVO reviewVO) {
        commentService.updateCommentsReview(reviewVO);
        return Result.ok();
    }

    @OptLog(optType = DELETE)
    @ApiOperation(value = "删除评论")
    @DeleteMapping("/admin/comments")
    public Result<?> deleteComments(@RequestBody List<Integer> commentIdList) {
        commentService.removeByIds(commentIdList);
        return Result.ok();
    }


}
