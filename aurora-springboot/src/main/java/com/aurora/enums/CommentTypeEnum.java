package com.aurora.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommentTypeEnum {

    ARTICLE(1, "文章", "/articles/"),

    MESSAGE(2, "留言", "/message/"),

    ABOUT(3, "关于我", "/about/"),

    LINK(4, "友链", "/friends/"),

    TALK(5, "说说", "/talks/");

    private final Integer type;

    private final String desc;

    private final String path;

    public static String getCommentPath(Integer type) {
        for (CommentTypeEnum value : CommentTypeEnum.values()) {
            if (value.getType().equals(type)) {
                return value.getPath();
            }
        }
        return null;
    }

    public static CommentTypeEnum getCommentEnum(Integer type) {
        for (CommentTypeEnum value : CommentTypeEnum.values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }

}
