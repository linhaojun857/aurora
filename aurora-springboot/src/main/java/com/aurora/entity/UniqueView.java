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

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 访问量
     */
    private Integer viewsCount;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

}
