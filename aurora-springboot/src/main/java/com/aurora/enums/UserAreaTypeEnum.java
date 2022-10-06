package com.aurora.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserAreaTypeEnum {

    USER(1, "用户"),

    VISITOR(2, "游客");

    private final Integer type;

    private final String desc;

    public static UserAreaTypeEnum getUserAreaType(Integer type) {
        for (UserAreaTypeEnum value : UserAreaTypeEnum.values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }

}
