package com.aurora.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FilePathEnum {

    AVATAR("aurora/avatar/", "头像路径"),

    ARTICLE("aurora/articles/", "文章图片路径"),

    VOICE("aurora/voice/", "音频路径"),

    PHOTO("aurora/photos/", "相册路径"),

    CONFIG("aurora/config/", "配置图片路径"),

    TALK("aurora/talks/", "配置图片路径"),

    MD("aurora/markdown/", "md文件路径");

    private final String path;

    private final String desc;

}
