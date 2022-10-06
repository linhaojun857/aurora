package com.aurora.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebsiteConfigDTO {

    private String name;

    private String englishName;

    private String author;

    private String authorAvatar;

    private String authorIntro;

    private String logo;

    private Integer multiLanguage;

    private String notice;

    private String websiteCreateTime;

    private String beianNumber;

    private Integer qqLogin;

    private String github;

    private String gitee;

    private String qq;

    private String weChat;

    private String weibo;

    private String csdn;

    private String zhihu;

    private String juejin;

    private String twitter;

    private String stackoverflow;

    private String touristAvatar;

    private String userAvatar;

    private Integer isCommentReview;

    private Integer isEmailNotice;

    private Integer isReward;

    private String weiXinQRCode;

    private String alipayQRCode;

}
