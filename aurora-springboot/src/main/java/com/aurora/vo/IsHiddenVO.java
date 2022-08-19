package com.aurora.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 花未眠
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IsHiddenVO {

    private Integer id;

    private Integer isHidden;
}
