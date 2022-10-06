package com.aurora.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleEnum {

    ADMIN(1, "管理员", "admin"),

    USER(2, "用户", "user"),

    TEST(3, "测试", "test");

    private final Integer roleId;

    private final String name;

    private final String label;

}
