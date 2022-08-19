package com.aurora.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 花未眠
 * 登入登出反馈信息类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLogoutStatusDTO {

    private String Message;
}
