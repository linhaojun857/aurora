package com.aurora.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaxwellDataDTO {

    private String database;

    private Integer xid;

    private Map<String, Object> data;

    private Boolean commit;

    private String type;

    private String table;

    private Integer ts;

}
