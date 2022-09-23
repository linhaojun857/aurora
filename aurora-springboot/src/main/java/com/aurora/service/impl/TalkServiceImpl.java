package com.aurora.service.impl;

import com.alibaba.fastjson.JSON;
import com.aurora.model.dto.CommentCountDTO;
import com.aurora.model.dto.TalkAdminDTO;
import com.aurora.model.dto.TalkDTO;
import com.aurora.entity.Talk;
import com.aurora.exception.BizException;
import com.aurora.mapper.CommentMapper;
import com.aurora.mapper.TalkMapper;
import com.aurora.service.TalkService;

import com.aurora.utils.BeanCopyUtils;
import com.aurora.utils.CommonUtils;
import com.aurora.utils.PageUtils;
import com.aurora.utils.UserUtils;
import com.aurora.model.vo.ConditionVO;
import com.aurora.model.vo.PageResult;
import com.aurora.model.vo.TalkVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.aurora.enums.TalkStatusEnum.PUBLIC;


@Service
public class TalkServiceImpl extends ServiceImpl<TalkMapper, Talk> implements TalkService {

    @Autowired
    private TalkMapper talkMapper;

    @Autowired
    private CommentMapper commentMapper;


    @Override
    public PageResult<TalkDTO> listTalks() {
        // 查询说说总量
        Integer count = talkMapper.selectCount((new LambdaQueryWrapper<Talk>()
                .eq(Talk::getStatus, PUBLIC.getStatus())));
        if (count == 0) {
            return new PageResult<>();
        }
        // 分页查询说说
        List<TalkDTO> talkDTOs = talkMapper.listTalks(PageUtils.getLimitCurrent(), PageUtils.getSize());
        // 查询说说评论量
        List<Integer> talkIds = talkDTOs.stream()
                .map(TalkDTO::getId)
                .collect(Collectors.toList());
        Map<Integer, Integer> commentCountMap = commentMapper.listCommentCountByTopicIds(talkIds)
                .stream()
                .collect(Collectors.toMap(CommentCountDTO::getId, CommentCountDTO::getCommentCount));
        // 查询说说点赞量
        talkDTOs.forEach(item -> {
            item.setCommentCount(commentCountMap.get(item.getId()));
            // 转换图片格式
            if (Objects.nonNull(item.getImages())) {
                item.setImgs(CommonUtils.castList(JSON.parseObject(item.getImages(), List.class), String.class));
            }
        });
        return new PageResult<>(talkDTOs, count);
    }

    @Override
    public TalkDTO getTalkById(Integer talkId) {
        // 查询说说信息
        TalkDTO talkDTO = talkMapper.getTalkById(talkId);
        if (Objects.isNull(talkDTO)) {
            throw new BizException("说说不存在");
        }
        // 转换图片格式
        if (Objects.nonNull(talkDTO.getImages())) {
            talkDTO.setImgs(CommonUtils.castList(JSON.parseObject(talkDTO.getImages(), List.class), String.class));
        }
        return talkDTO;
    }

    @Override
    public void saveOrUpdateTalk(TalkVO talkVO) {
        Talk talk = BeanCopyUtils.copyObject(talkVO, Talk.class);
        talk.setUserId(UserUtils.getUserDetailsDTO().getUserInfoId());
        this.saveOrUpdate(talk);
    }

    @Override
    public void deleteTalks(List<Integer> talkIdList) {
        talkMapper.deleteBatchIds(talkIdList);
    }

    @Override
    public PageResult<TalkAdminDTO> listBackTalks(ConditionVO conditionVO) {
        // 查询说说总量
        Integer count = talkMapper.selectCount(new LambdaQueryWrapper<Talk>()
                .eq(Objects.nonNull(conditionVO.getStatus()), Talk::getStatus, conditionVO.getStatus()));
        if (count == 0) {
            return new PageResult<>();
        }
        // 分页查询说说
        List<TalkAdminDTO> talkDTOList = talkMapper.listTalksAdmin(PageUtils.getLimitCurrent(), PageUtils.getSize(), conditionVO);
        talkDTOList.forEach(item -> {
            // 转换图片格式
            if (Objects.nonNull(item.getImages())) {
                item.setImgs(CommonUtils.castList(JSON.parseObject(item.getImages(), List.class), String.class));
            }
        });
        return new PageResult<>(talkDTOList, count);
    }

    @Override
    public TalkAdminDTO getBackTalkById(Integer talkId) {
        TalkAdminDTO talkBackDTO = talkMapper.getTalkByIdAdmin(talkId);
        // 转换图片格式
        if (Objects.nonNull(talkBackDTO.getImages())) {
            talkBackDTO.setImgs(CommonUtils.castList(JSON.parseObject(talkBackDTO.getImages(), List.class), String.class));
        }
        return talkBackDTO;
    }
}

