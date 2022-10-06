package com.aurora.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MarkdownTypeEnum {

    NORMAL("", "normalArticleImportStrategyImpl");

    private final String type;

    private final String strategy;

    public static String getMarkdownType(String name) {
        if (name == null) {
            return NORMAL.getStrategy();
        }
        for (MarkdownTypeEnum value : MarkdownTypeEnum.values()) {
            if (value.getType().equalsIgnoreCase(name)) {
                return value.getStrategy();
            }
        }
        return null;
    }
}
