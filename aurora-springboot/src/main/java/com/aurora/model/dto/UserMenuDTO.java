package com.aurora.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserMenuDTO {

    private String name;

    private String path;

    private String component;

    private String icon;

    private Boolean hidden;

    private List<UserMenuDTO> children;

}
