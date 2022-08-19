package com.aurora.utils;

import com.aurora.dto.UserDetailDTO;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


/**
 * @author 花未眠
 * 用户工具类
 */
@Component
public class UserUtils {

    /**
     * 获取当前登录用户
     * @return 用户登录信息
     */
    public static UserDetailDTO getLoginUser() {
        return (UserDetailDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
