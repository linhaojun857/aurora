package com.aurora.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 花未眠
 * 评论的实体类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_comment")
public class Comment {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Integer replyUserId;
    private Integer topicId;
    private String commentContent;
    private Integer parentId;
    /**
     * 类型：1.文章 2.留言 3.关于我 4.友链 5.说说
     */
    private Integer type;
    private Integer isDelete;
    private Integer isReview;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
}
