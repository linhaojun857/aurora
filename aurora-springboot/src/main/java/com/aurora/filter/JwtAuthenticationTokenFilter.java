package com.aurora.filter;


import com.aurora.dto.UserDetailDTO;
import com.aurora.exception.BizException;
import com.aurora.service.RedisService;
import com.aurora.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

import static com.aurora.constant.RedisPrefixConst.TOKEN_BLACKLIST;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    public RedisService redisService;

    @Autowired
    public AuthenticationEntryPoint authenticationEntryPoint;


    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        String userId;
        try {
            Claims claims = JwtUtils.parseJWT(token);
            userId = claims.getSubject();
        } catch (Exception e) {
            throwBizException(request, response, "token非法或者超时!");
            return;
        }
        Boolean isMember = redisService.sIsMember(TOKEN_BLACKLIST, token);
        if (isMember.equals(true)) {
            throwBizException(request, response, "用户未登录!");
            return;
        }
        UserDetailDTO user = (UserDetailDTO) redisService.hGet("login_user", userId);
        if (Objects.isNull(user)) {
            throwBizException(request, response, "用户未登录!");
            return;
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }

    @SneakyThrows
    public void throwBizException(HttpServletRequest request, HttpServletResponse response, String message) {
        request.setAttribute("bizException", new BizException(message));
        request.getRequestDispatcher("/bizException").forward(request, response);
    }
}
