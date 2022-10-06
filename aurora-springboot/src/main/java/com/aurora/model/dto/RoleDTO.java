package com.aurora.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {

    private Integer id;

    private String roleName;

    private LocalDateTime createTime;

    private Integer isDisable;

    private List<Integer> resourceIds;

    private List<Integer> menuIds;

}
