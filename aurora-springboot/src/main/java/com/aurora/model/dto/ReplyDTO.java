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
public class ReplyDTO {

    private Integer id;

    private Integer parentId;

    private Integer userId;

    private String nickname;

    private String avatar;

    private String webSite;

    private Integer replyUserId;

    private String replyNickname;

    private String replyWebsite;

    private String commentContent;

    private LocalDateTime createTime;

}
