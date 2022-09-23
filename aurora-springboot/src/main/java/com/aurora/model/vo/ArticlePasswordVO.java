package com.aurora.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticlePasswordVO {
    private Integer articleId;
    private String articlePassword;
}
