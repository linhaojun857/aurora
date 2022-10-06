package com.aurora.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhotoAdminDTO {

    private Integer id;

    private String photoName;

    private String photoDesc;

    private String photoSrc;

}
