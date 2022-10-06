package com.aurora.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhotoAlbumDTO {

    private Integer id;

    private String albumName;

    private String albumDesc;

    private String albumCover;

}
