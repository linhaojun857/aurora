package com.aurora.strategy;


import com.aurora.model.dto.UserInfoDTO;

/**
 * @author 花未眠
 * 第三方登录策略
 */
public interface SocialLoginStrategy {

    /**
     * 登录
     */
    UserInfoDTO login(String data);

}
