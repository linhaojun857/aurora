package com.aurora.service.impl;

import com.aurora.model.dto.UserDetailsDTO;
import com.aurora.model.dto.UserInfoDTO;
import com.aurora.model.dto.UserOnlineDTO;
import com.aurora.entity.UserAuth;
import com.aurora.entity.UserInfo;
import com.aurora.entity.UserRole;
import com.aurora.enums.FilePathEnum;
import com.aurora.exception.BizException;
import com.aurora.mapper.UserAuthMapper;
import com.aurora.mapper.UserInfoMapper;
import com.aurora.service.RedisService;
import com.aurora.service.TokenService;
import com.aurora.service.UserInfoService;
import com.aurora.service.UserRoleService;
import com.aurora.strategy.context.UploadStrategyContext;
import com.aurora.util.BeanCopyUtil;
import com.aurora.util.UserUtil;
import com.aurora.model.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

import static com.aurora.constant.RedisPrefixConstant.USER_CODE_KEY;
import static com.aurora.util.PageUtil.getLimitCurrent;
import static com.aurora.util.PageUtil.getSize;


@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserAuthMapper userAuthMapper;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UploadStrategyContext uploadStrategyContext;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserInfo(UserInfoVO userInfoVO) {
        // 封装用户信息
        UserInfo userInfo = UserInfo.builder()
                .id(UserUtil.getUserDetailsDTO().getUserInfoId())
                .nickname(userInfoVO.getNickname())
                .intro(userInfoVO.getIntro())
                .website(userInfoVO.getWebsite())
                .build();
        userInfoMapper.updateById(userInfo);
    }

    @Override
    public String updateUserAvatar(MultipartFile file) {
        // 头像上传
        String avatar = uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.AVATAR.getPath());
        // 更新用户信息
        UserInfo userInfo = UserInfo.builder()
                .id(UserUtil.getUserDetailsDTO().getUserInfoId())
                .avatar(avatar)
                .build();
        userInfoMapper.updateById(userInfo);
        return avatar;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveUserEmail(EmailVO emailVO) {
        if (Objects.isNull(redisService.get(USER_CODE_KEY + emailVO.getEmail()))) {
            throw new BizException("验证码错误");
        }
        if (!emailVO.getCode().equals(redisService.get(USER_CODE_KEY + emailVO.getEmail()).toString())) {
            throw new BizException("验证码错误！");
        }
        UserInfo userInfo = UserInfo.builder()
                .id(UserUtil.getUserDetailsDTO().getUserInfoId())
                .email(emailVO.getEmail())
                .build();
        userInfoMapper.updateById(userInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserSubscribe(SubscribeVO subscribeVO) {
        UserInfo temp = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getId, subscribeVO.getUserId()));
        if (StringUtils.isEmpty(temp.getEmail())) {
            throw new BizException("邮箱未绑定！");
        }
        UserInfo userInfo = UserInfo.builder()
                .id(subscribeVO.getUserId())
                .isSubscribe(subscribeVO.getIsSubscribe())
                .build();
        userInfoMapper.updateById(userInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserRole(UserRoleVO userRoleVO) {
        // 更新用户角色和昵称
        UserInfo userInfo = UserInfo.builder()
                .id(userRoleVO.getUserInfoId())
                .nickname(userRoleVO.getNickname())
                .build();
        userInfoMapper.updateById(userInfo);
        // 删除用户角色重新添加
        userRoleService.remove(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId, userRoleVO.getUserInfoId()));
        List<UserRole> userRoleList = userRoleVO.getRoleIds().stream()
                .map(roleId -> UserRole.builder()
                        .roleId(roleId)
                        .userId(userRoleVO.getUserInfoId())
                        .build())
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoleList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserDisable(UserDisableVO userDisableVO) {
        // 更新用户禁用状态
        UserInfo userInfo = UserInfo.builder()
                .id(userDisableVO.getId())
                .isDisable(userDisableVO.getIsDisable())
                .build();
        userInfoMapper.updateById(userInfo);
    }

    @Override
    public PageResult<UserOnlineDTO> listOnlineUsers(ConditionVO conditionVO) {
        Map<String, Object> userMaps = redisService.hGetAll("login_user");
        Collection<Object> values = userMaps.values();
        ArrayList<UserDetailsDTO> userDetailsDTOs = new ArrayList<>();
        for (Object value : values) {
            userDetailsDTOs.add((UserDetailsDTO) value);
        }
        List<UserOnlineDTO> userOnlineDTOs = BeanCopyUtil.copyList(userDetailsDTOs, UserOnlineDTO.class);
        List<UserOnlineDTO> onlineUsers = userOnlineDTOs.stream()
                .filter(item -> StringUtils.isBlank(conditionVO.getKeywords()) || item.getNickname().contains(conditionVO.getKeywords()))
                .sorted(Comparator.comparing(UserOnlineDTO::getLastLoginTime).reversed())
                .collect(Collectors.toList());
        int fromIndex = getLimitCurrent().intValue();
        int size = getSize().intValue();
        int toIndex = onlineUsers.size() - fromIndex > size ? fromIndex + size : onlineUsers.size();
        List<UserOnlineDTO> userOnlineList = onlineUsers.subList(fromIndex, toIndex);
        return new PageResult<>(userOnlineList, onlineUsers.size());
    }

    @Override
    public void removeOnlineUser(Integer userInfoId) {
        Integer userId = userAuthMapper.selectOne(new LambdaQueryWrapper<UserAuth>().eq(UserAuth::getUserInfoId, userInfoId)).getId();
        tokenService.delLoginUser(userId);
    }

    @Override
    public UserInfoDTO getUserInfoById(Integer id) {
        UserInfo userInfo = userInfoMapper.selectById(id);
        return BeanCopyUtil.copyObject(userInfo, UserInfoDTO.class);
    }
}
