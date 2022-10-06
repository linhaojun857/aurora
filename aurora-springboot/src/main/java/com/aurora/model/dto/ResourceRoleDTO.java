package com.aurora.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class ResourceRoleDTO {

    private Integer id;

    private String url;

    private String requestMethod;

    private List<String> roleList;

}
