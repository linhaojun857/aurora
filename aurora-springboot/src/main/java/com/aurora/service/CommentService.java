package com.aurora.service;

import com.aurora.dto.CommentAdminDTO;
import com.aurora.dto.CommentDTO;
import com.aurora.dto.ReplyDTO;
import com.aurora.entity.Comment;
import com.aurora.vo.CommentVO;
import com.aurora.vo.ConditionVO;
import com.aurora.vo.PageResult;
import com.aurora.vo.ReviewVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface CommentService extends IService<Comment> {

    void saveComment(CommentVO commentVO);

    PageResult<CommentDTO> listComments(CommentVO commentVO);

    List<ReplyDTO> listRepliesByCommentId(Integer commentId);

    List<CommentDTO> listTopSevenComments();

    PageResult<CommentAdminDTO> listCommentsAdmin(ConditionVO conditionVO);

    void updateCommentsReview(ReviewVO reviewVO);

}
