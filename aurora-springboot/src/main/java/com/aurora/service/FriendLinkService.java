package com.aurora.service;

import com.aurora.dto.FriendLinkAdminDTO;
import com.aurora.dto.FriendLinkDTO;
import com.aurora.entity.FriendLink;
import com.aurora.vo.ConditionVO;
import com.aurora.vo.FriendLinkVO;
import com.aurora.vo.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface FriendLinkService extends IService<FriendLink> {

    List<FriendLinkDTO> listFriendLinks();

    PageResult<FriendLinkAdminDTO> listFriendLinksAdmin(ConditionVO conditionVO);

    void saveOrUpdateFriendLink(FriendLinkVO friendLinkVO);
}
