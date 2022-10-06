package com.aurora.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendLinkDTO {

    private Integer id;

    private String linkName;

    private String linkAvatar;

    private String linkAddress;

    private String linkIntro;

}
