package com.aurora.strategy.impl;

import com.alibaba.fastjson.JSON;
import com.aurora.config.properties.QQConfigProperties;
import com.aurora.constant.SocialLoginConst;
import com.aurora.model.dto.QQTokenDTO;
import com.aurora.model.dto.QQUserInfoDTO;
import com.aurora.model.dto.SocialTokenDTO;
import com.aurora.model.dto.SocialUserInfoDTO;
import com.aurora.enums.LoginTypeEnum;
import com.aurora.exception.BizException;
import com.aurora.utils.CommonUtils;
import com.aurora.model.vo.QQLoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.aurora.constant.SocialLoginConst.*;
import static com.aurora.enums.StatusCodeEnum.QQ_LOGIN_ERROR;


/**
 * @author 花未眠
 * qq登录策略实现
 */
@Service("qqLoginStrategyImpl")
public class QQLoginStrategyImpl extends AbstractSocialLoginStrategyImpl {

    @Autowired
    private QQConfigProperties qqConfigProperties;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public SocialTokenDTO getSocialToken(String data) {
        QQLoginVO qqLoginVO = JSON.parseObject(data, QQLoginVO.class);
        // 校验QQ token信息
        checkQQToken(qqLoginVO);
        // 返回token信息
        return SocialTokenDTO.builder()
                .openId(qqLoginVO.getOpenId())
                .accessToken(qqLoginVO.getAccessToken())
                .loginType(LoginTypeEnum.QQ.getType())
                .build();
    }

    @Override
    public SocialUserInfoDTO getSocialUserInfo(SocialTokenDTO socialTokenDTO) {
        // 定义请求参数
        Map<String, String> formData = new HashMap<>(3);
        formData.put(QQ_OPEN_ID, socialTokenDTO.getOpenId());
        formData.put(ACCESS_TOKEN, socialTokenDTO.getAccessToken());
        formData.put(OAUTH_CONSUMER_KEY, qqConfigProperties.getAppId());
        // 获取QQ返回的用户信息
        QQUserInfoDTO qqUserInfoDTO = JSON.parseObject(restTemplate.getForObject(qqConfigProperties.getUserInfoUrl(), String.class, formData), QQUserInfoDTO.class);
        // 返回用户信息
        return SocialUserInfoDTO.builder()
                .nickname(Objects.requireNonNull(qqUserInfoDTO).getNickname())
                .avatar(qqUserInfoDTO.getFigureurl_qq_1())
                .build();
    }

    /**
     * 校验qq token信息
     *
     * @param qqLoginVO qq登录信息
     */
    private void checkQQToken(QQLoginVO qqLoginVO) {
        // 根据token获取qq openId信息
        Map<String, String> qqData = new HashMap<>(1);
        qqData.put(SocialLoginConst.ACCESS_TOKEN, qqLoginVO.getAccessToken());
        try {
            String result = restTemplate.getForObject(qqConfigProperties.getCheckTokenUrl(), String.class, qqData);
            QQTokenDTO qqTokenDTO = JSON.parseObject(CommonUtils.getBracketsContent(Objects.requireNonNull(result)), QQTokenDTO.class);
            // 判断openId是否一致
            if (!qqLoginVO.getOpenId().equals(qqTokenDTO.getOpenid())) {
                throw new BizException(QQ_LOGIN_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(QQ_LOGIN_ERROR);
        }
    }

}
