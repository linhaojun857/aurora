package com.aurora.service.impl;

import com.aurora.dto.FriendLinkAdminDTO;
import com.aurora.dto.FriendLinkDTO;
import com.aurora.entity.FriendLink;
import com.aurora.mapper.FriendLinkMapper;
import com.aurora.service.FriendLinkService;
import com.aurora.utils.BeanCopyUtils;
import com.aurora.utils.PageUtils;
import com.aurora.vo.ConditionVO;
import com.aurora.vo.FriendLinkVO;
import com.aurora.vo.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FriendLinkServiceImpl extends ServiceImpl<FriendLinkMapper, FriendLink> implements FriendLinkService {

    @Autowired
    private FriendLinkMapper friendLinkMapper;

    @Override
    public List<FriendLinkDTO> listFriendLinks() {
        List<FriendLink> friendLinks = friendLinkMapper.selectList(null);
        return BeanCopyUtils.copyList(friendLinks, FriendLinkDTO.class);
    }

    @Override
    public PageResult<FriendLinkAdminDTO> listFriendLinksAdmin(ConditionVO conditionVO) {
        Page<FriendLink> page = new Page<>(PageUtils.getCurrent(), PageUtils.getSize());
        Page<FriendLink> friendLinkPage = friendLinkMapper.selectPage(page, new LambdaQueryWrapper<FriendLink>()
                .like(StringUtils.isNotBlank(conditionVO.getKeywords()), FriendLink::getLinkName, conditionVO.getKeywords()));
        List<FriendLinkAdminDTO> friendLinkBackDTOs = BeanCopyUtils.copyList(friendLinkPage.getRecords(), FriendLinkAdminDTO.class);
        return new PageResult<>(friendLinkBackDTOs, (int) friendLinkPage.getTotal());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateFriendLink(FriendLinkVO friendLinkVO) {
        FriendLink friendLink = BeanCopyUtils.copyObject(friendLinkVO, FriendLink.class);
        this.saveOrUpdate(friendLink);
    }
}
