package com.aurora.service;

import com.aurora.model.dto.UserAdminDTO;
import com.aurora.model.dto.UserAreaDTO;
import com.aurora.model.dto.UserInfoDTO;
import com.aurora.model.dto.UserLogoutStatusDTO;
import com.aurora.model.vo.*;

import java.util.List;

public interface UserAuthService {

    void sendCode(String username);

    List<UserAreaDTO> listUserAreas(ConditionVO conditionVO);

    void register(UserVO userVO);

    void updatePassword(UserVO userVO);

    void updateAdminPassword(PasswordVO passwordVO);

    PageResult<UserAdminDTO> listUsers(ConditionVO condition);

    UserLogoutStatusDTO logout();

    UserInfoDTO qqLogin(QQLoginVO qqLoginVO);

}
