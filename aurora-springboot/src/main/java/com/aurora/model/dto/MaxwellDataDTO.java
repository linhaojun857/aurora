package com.aurora.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author 花未眠
 * maxwell监听数据
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaxwellDataDTO {

    /**
     * 数据库
     */
    private String database;

    /**
     * xid
     */
    private Integer xid;

    /**
     * 数据
     */
    private Map<String, Object> data;

    /**
     * 是否提交
     */
    private Boolean commit;

    /**
     * 类型
     */
    private String type;

    /**
     * 表
     */
    private String table;

    /**
     * ts
     */
    private Integer ts;

}
