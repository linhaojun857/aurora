package com.aurora.service;

import com.aurora.dto.TalkAdminDTO;
import com.aurora.dto.TalkDTO;
import com.aurora.entity.Talk;
import com.aurora.vo.ConditionVO;
import com.aurora.vo.PageResult;
import com.aurora.vo.TalkVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface TalkService extends IService<Talk> {

    PageResult<TalkDTO> listTalks();

    TalkDTO getTalkById(Integer talkId);

    void saveOrUpdateTalk(TalkVO talkVO);

    void deleteTalks(List<Integer> talkIdList);

    PageResult<TalkAdminDTO> listBackTalks(ConditionVO conditionVO);

    TalkAdminDTO getBackTalkById(Integer talkId);
}
