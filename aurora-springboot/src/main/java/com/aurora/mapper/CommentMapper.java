package com.aurora.mapper;

import com.aurora.dto.CommentAdminDTO;
import com.aurora.dto.CommentCountDTO;
import com.aurora.dto.CommentDTO;
import com.aurora.dto.ReplyDTO;
import com.aurora.entity.Comment;
import com.aurora.vo.CommentVO;
import com.aurora.vo.ConditionVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentMapper extends BaseMapper<Comment> {

    List<CommentDTO> listComments(@Param("current") Long current, @Param("size") Long size, @Param("commentVO") CommentVO commentVO);

    List<ReplyDTO> listReplies(@Param("commentIds") List<Integer> commentIdList);

    List<CommentDTO> listTopSixComments();

    Integer countComments(@Param("conditionVO") ConditionVO conditionVO);

    List<CommentAdminDTO> listCommentsAdmin(@Param("current") Long current, @Param("size") Long size, @Param("conditionVO") ConditionVO conditionVO);

    List<CommentCountDTO> listCommentCountByTopicIds(@Param("topicIds") List<Integer> topicIds);
}
