package com.aurora.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TalkStatusEnum {

    PUBLIC(1, "公开"),

    SECRET(2, "私密");

    private final Integer status;

    private final String desc;

}
