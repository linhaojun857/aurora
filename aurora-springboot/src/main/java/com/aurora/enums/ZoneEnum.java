package com.aurora.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 花未眠
 * 时区枚举类
 */
@Getter
@AllArgsConstructor
public enum ZoneEnum {

    SHANGHAI("Asia/Shanghai", "中国上海");


    private final String zone;


    private final String desc;
}
