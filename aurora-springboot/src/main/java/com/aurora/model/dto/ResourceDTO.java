package com.aurora.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceDTO {

    private Integer id;

    private String resourceName;

    private String url;

    private String requestMethod;

    private Integer isDisable;

    private Integer isAnonymous;

    private LocalDateTime createTime;

    private List<ResourceDTO> children;

}
