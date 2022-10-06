package com.aurora.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryAdminDTO {

    private Integer id;

    private String categoryName;

    private Integer articleCount;

    private LocalDateTime createTime;

}
