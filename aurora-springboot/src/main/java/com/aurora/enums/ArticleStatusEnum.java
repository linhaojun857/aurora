package com.aurora.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ArticleStatusEnum {

    PUBLIC(1, "公开"),

    SECRET(2, "密码"),

    DRAFT(3, "草稿");

    private final Integer status;

    private final String desc;

}
