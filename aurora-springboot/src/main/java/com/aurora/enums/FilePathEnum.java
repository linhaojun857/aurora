package com.aurora.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FilePathEnum {

    AVATAR("avatar/", "头像路径"),

    ARTICLE("articles/", "文章图片路径"),

    VOICE("voice/", "音频路径"),

    PHOTO("photos/", "相册路径"),

    CONFIG("config/", "配置图片路径"),

    TALK("talks/", "配置图片路径"),

    MD("markdown/", "md文件路径");

    private final String path;

    private final String desc;

}
