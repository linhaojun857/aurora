package com.aurora.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentAdminDTO {

    private Integer id;

    private String avatar;

    private String nickname;

    private String replyNickname;

    private String articleTitle;

    private String commentContent;

    private Integer type;

    private Integer isReview;

    private LocalDateTime createTime;

}
