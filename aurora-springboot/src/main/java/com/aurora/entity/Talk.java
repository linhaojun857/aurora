package com.aurora.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author 花未眠
 * 说说
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_talk")
public class Talk {

    /**
     * 说说id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 说说内容
     */
    private String content;

    /**
     * 图片
     */
    private String images;

    /**
     * 是否置顶
     */
    private Integer isTop;

    /**
     * 说说状态 1.公开 2.私密
     */
    private Integer status;

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