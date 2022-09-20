package com.aurora.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 花未眠
 * 文章状态枚举
 */
@Getter
@AllArgsConstructor
public enum ArticleStatusEnum {
    /**
     * 公开
     */
    PUBLIC(1, "公开"),
    /**
     * 密码
     */
    SECRET(2, "密码"),
    /**
     * 草稿
     */
    DRAFT(3, "草稿");

    /**
     * 状态
     */
    private final Integer status;

    /**
     * 描述
     */
    private final String desc;

}
