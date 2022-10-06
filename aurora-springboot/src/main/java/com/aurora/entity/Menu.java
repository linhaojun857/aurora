package com.aurora.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_menu")
public class Menu {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private String path;

    private String component;

    private String icon;

    private Integer orderNum;

    private Integer parentId;

    private Integer isHidden;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

}

