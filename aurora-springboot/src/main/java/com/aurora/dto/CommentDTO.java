package com.aurora.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


/**
 * @author 花未眠
 * 评论的DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {

    /**
     * 评论id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 个人网站
     */
    private String webSite;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 评论时间
     */
    private LocalDateTime createTime;

    /**
     * 回复列表
     */
    private List<ReplyDTO> replyDTOs;

}
