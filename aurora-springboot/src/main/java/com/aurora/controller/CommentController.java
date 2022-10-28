package com.aurora.controller;

import com.aurora.annotation.AccessLimit;
import com.aurora.annotation.OptLog;
import com.aurora.model.dto.CommentAdminDTO;
import com.aurora.model.dto.CommentDTO;
import com.aurora.model.dto.PageResultDTO;
import com.aurora.model.dto.ReplyDTO;
import com.aurora.service.CommentService;
import com.aurora.model.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.aurora.constant.OptTypeConstant.*;

@Api(tags = "评论模块")
@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @AccessLimit(seconds = 60, maxCount = 3)
    @OptLog(optType = SAVE)
    @ApiOperation("添加评论")
    @PostMapping("/comments/save")
    public ResultVO<?> saveComment(@Valid @RequestBody CommentVO commentVO) {
        commentService.saveComment(commentVO);
        return ResultVO.ok();
    }

    @ApiOperation("获取评论")
    @GetMapping("/comments")
    public ResultVO<PageResultDTO<CommentDTO>> getComments(CommentVO commentVO) {
        return ResultVO.ok(commentService.listComments(commentVO));
    }

    @ApiOperation(value = "根据commentId获取回复")
    @GetMapping("/comments/{commentId}/replies")
    public ResultVO<List<ReplyDTO>> listRepliesByCommentId(@PathVariable("commentId") Integer commentId) {
        return ResultVO.ok(commentService.listRepliesByCommentId(commentId));
    }

    @ApiOperation("获取前六个评论")
    @GetMapping("/comments/topSix")
    public ResultVO<List<CommentDTO>> listTopSixComments() {
        return ResultVO.ok(commentService.listTopSixComments());
    }

    @ApiOperation(value = "查询后台评论")
    @GetMapping("/admin/comments")
    public ResultVO<PageResultDTO<CommentAdminDTO>> listCommentBackDTO(ConditionVO conditionVO) {
        return ResultVO.ok(commentService.listCommentsAdmin(conditionVO));
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "审核评论")
    @PutMapping("/admin/comments/review")
    public ResultVO<?> updateCommentsReview(@Valid @RequestBody ReviewVO reviewVO) {
        commentService.updateCommentsReview(reviewVO);
        return ResultVO.ok();
    }

    @OptLog(optType = DELETE)
    @ApiOperation(value = "删除评论")
    @DeleteMapping("/admin/comments")
    public ResultVO<?> deleteComments(@RequestBody List<Integer> commentIdList) {
        commentService.removeByIds(commentIdList);
        return ResultVO.ok();
    }

}
