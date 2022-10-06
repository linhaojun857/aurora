package com.aurora.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TagAdminDTO {

    private Integer id;

    private String tagName;

    private Integer articleCount;

    private LocalDateTime createTime;

}
