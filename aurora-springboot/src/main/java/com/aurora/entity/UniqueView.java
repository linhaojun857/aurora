package com.aurora.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;

import lombok.*;

/**
 * @author 花未眠
 * 网站访问量
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_unique_view")
public class UniqueView {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer viewsCount;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

}
