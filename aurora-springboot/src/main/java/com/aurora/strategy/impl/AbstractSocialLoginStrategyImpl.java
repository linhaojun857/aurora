package com.aurora.strategy.impl;

import com.aurora.dto.SocialTokenDTO;
import com.aurora.dto.SocialUserInfoDTO;
import com.aurora.dto.UserDetailsDTO;
import com.aurora.dto.UserInfoDTO;
import com.aurora.entity.UserAuth;
import com.aurora.entity.UserInfo;
import com.aurora.entity.UserRole;
import com.aurora.enums.RoleEnum;
import com.aurora.exception.BizException;
import com.aurora.mapper.UserAuthMapper;
import com.aurora.mapper.UserInfoMapper;
import com.aurora.mapper.UserRoleMapper;
import com.aurora.service.TokenService;
import com.aurora.service.impl.UserDetailServiceImpl;
import com.aurora.strategy.SocialLoginStrategy;
import com.aurora.utils.BeanCopyUtils;
import com.aurora.utils.IpUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.aurora.constant.CommonConst.TRUE;


/**
 * @author 花未眠
 * 第三方登录抽象模板
 */
@Service
public abstract class AbstractSocialLoginStrategyImpl implements SocialLoginStrategy {

    @Autowired
    private UserAuthMapper userAuthMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private TokenService tokenService;

    @Resource
    private HttpServletRequest request;

    @Override
    public UserInfoDTO login(String data) {
        // 创建登录信息
        UserDetailsDTO userDetailsDTO;
        // 获取第三方token信息
        SocialTokenDTO socialToken = getSocialToken(data);
        // 获取用户ip信息
        String ipAddress = IpUtils.getIpAddress(request);
        String ipSource = IpUtils.getIpSource(ipAddress);
        // 判断是否已注册
        UserAuth user = getUserAuth(socialToken);
        if (Objects.nonNull(user)) {
            // 返回数据库用户信息
            userDetailsDTO = getUserDetail(user, ipAddress, ipSource);
        } else {
            // 获取第三方用户信息，保存到数据库返回
            userDetailsDTO = saveUserDetail(socialToken, ipAddress, ipSource);

        }
        // 判断账号是否禁用
        if (userDetailsDTO.getIsDisable().equals(TRUE)) {
            throw new BizException("用户帐号已被锁定");
        }
        // 将登录信息放入springSecurity管理
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetailsDTO, null, userDetailsDTO.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        // 返回用户信息
        UserInfoDTO userInfoDTO = BeanCopyUtils.copyObject(userDetailsDTO, UserInfoDTO.class);
        String token = tokenService.createToken(userDetailsDTO);
        userInfoDTO.setToken(token);
        return userInfoDTO;
    }

    /**
     * 获取第三方token信息
     *
     * @param data 数据
     * @return {@link SocialTokenDTO} 第三方token信息
     */
    public abstract SocialTokenDTO getSocialToken(String data);

    /**
     * 获取第三方用户信息
     *
     * @param socialTokenDTO 第三方token信息
     * @return {@link SocialUserInfoDTO} 第三方用户信息
     */
    public abstract SocialUserInfoDTO getSocialUserInfo(SocialTokenDTO socialTokenDTO);

    /**
     * 获取用户账号
     *
     * @return {@link UserAuth} 用户账号
     */
    private UserAuth getUserAuth(SocialTokenDTO socialTokenDTO) {
        return userAuthMapper.selectOne(new LambdaQueryWrapper<UserAuth>()
                .eq(UserAuth::getUsername, socialTokenDTO.getOpenId())
                .eq(UserAuth::getLoginType, socialTokenDTO.getLoginType()));
    }

    /**
     * 获取用户信息
     *
     * @param user      用户账号
     * @param ipAddress ip地址
     * @param ipSource  ip源
     * @return {@link UserDetailsDTO} 用户信息
     */
    private UserDetailsDTO getUserDetail(UserAuth user, String ipAddress, String ipSource) {
        // 更新登录信息
        userAuthMapper.update(new UserAuth(), new LambdaUpdateWrapper<UserAuth>()
                .set(UserAuth::getLastLoginTime, LocalDateTime.now())
                .set(UserAuth::getIpAddress, ipAddress)
                .set(UserAuth::getIpSource, ipSource)
                .eq(UserAuth::getId, user.getId()));
        // 封装信息
        return userDetailService.convertUserDetail(user, request);
    }

    /**
     * 新增用户信息
     *
     * @param socialToken token信息
     * @param ipAddress   ip地址
     * @param ipSource    ip源
     * @return {@link UserDetailsDTO} 用户信息
     */
    private UserDetailsDTO saveUserDetail(SocialTokenDTO socialToken, String ipAddress, String ipSource) {
        // 获取第三方用户信息
        SocialUserInfoDTO socialUserInfo = getSocialUserInfo(socialToken);
        // 保存用户信息
        UserInfo userInfo = UserInfo.builder()
                .nickname(socialUserInfo.getNickname())
                .avatar(socialUserInfo.getAvatar())
                .build();
        userInfoMapper.insert(userInfo);
        // 保存账号信息
        UserAuth userAuth = UserAuth.builder()
                .userInfoId(userInfo.getId())
                .username(socialToken.getOpenId())
                .password(socialToken.getAccessToken())
                .loginType(socialToken.getLoginType())
                .lastLoginTime(LocalDateTime.now())
                .ipAddress(ipAddress)
                .ipSource(ipSource)
                .build();
        userAuthMapper.insert(userAuth);
        // 绑定角色
        UserRole userRole = UserRole.builder()
                .userId(userInfo.getId())
                .roleId(RoleEnum.USER.getRoleId())
                .build();
        userRoleMapper.insert(userRole);
        return userDetailService.convertUserDetail(userAuth, request);
    }

}
