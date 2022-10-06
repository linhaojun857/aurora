package com.aurora.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {

    private Integer id;

    private Integer userId;

    private String nickname;

    private String avatar;

    private String webSite;

    private String commentContent;

    private LocalDateTime createTime;

    private List<ReplyDTO> replyDTOs;

}
