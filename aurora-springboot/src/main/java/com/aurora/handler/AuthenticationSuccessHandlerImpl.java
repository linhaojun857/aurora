package com.aurora.handler;

import com.alibaba.fastjson.JSON;
import com.aurora.constant.CommonConst;
import com.aurora.dto.UserDetailDTO;
import com.aurora.dto.UserInfoDTO;
import com.aurora.entity.UserAuth;
import com.aurora.mapper.UserAuthMapper;
import com.aurora.service.RedisService;
import com.aurora.utils.BeanCopyUtils;
import com.aurora.utils.JwtUtils;
import com.aurora.utils.UserUtils;
import com.aurora.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;


@Component
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    @Autowired
    private UserAuthMapper userAuthMapper;

    @Autowired
    private RedisService redisService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserInfoDTO userLoginDTO = BeanCopyUtils.copyObject(UserUtils.getLoginUser(), UserInfoDTO.class);
        if (Objects.nonNull(authentication)) {
            UserDetailDTO user = (UserDetailDTO) authentication.getPrincipal();
            String id = user.getId().toString();
            String jwt = JwtUtils.createJWT(id);
            redisService.hSet("login_user", id, user);
            userLoginDTO.setToken(jwt);
        }
        response.setContentType(CommonConst.APPLICATION_JSON);
        response.getWriter().write(JSON.toJSONString(Result.ok(userLoginDTO)));
        updateUserInfo();
    }

    @Async
    public void updateUserInfo() {
        UserAuth userAuth = UserAuth.builder()
                .id(UserUtils.getLoginUser().getId())
                .ipAddress(UserUtils.getLoginUser().getIpAddress())
                .ipSource(UserUtils.getLoginUser().getIpSource())
                .lastLoginTime(UserUtils.getLoginUser().getLastLoginTime())
                .build();
        userAuthMapper.updateById(userAuth);
    }
}
