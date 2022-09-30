package com.aurora.service.impl;


import com.aurora.model.dto.TagAdminDTO;
import com.aurora.model.dto.TagDTO;
import com.aurora.entity.ArticleTag;
import com.aurora.entity.Tag;
import com.aurora.exception.BizException;
import com.aurora.mapper.ArticleTagMapper;
import com.aurora.mapper.TagMapper;
import com.aurora.service.TagService;
import com.aurora.util.BeanCopyUtil;
import com.aurora.util.PageUtil;
import com.aurora.model.vo.ConditionVO;
import com.aurora.model.vo.PageResult;
import com.aurora.model.vo.TagVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Override
    public List<TagDTO> listTags() {
        return tagMapper.listTags();
    }

    @Override
    public List<TagDTO> listTopTenTags() {
        return tagMapper.listTopTenTags();
    }

    @SneakyThrows
    @Override
    public PageResult<TagAdminDTO> listTagsAdmin(ConditionVO conditionVO) {
        // 查询标签数量
        Integer count = tagMapper.selectCount(new LambdaQueryWrapper<Tag>()
                .like(StringUtils.isNotBlank(conditionVO.getKeywords()), Tag::getTagName, conditionVO.getKeywords()));
        if (count == 0) {
            return new PageResult<>();
        }
        // 分页查询标签列表
        List<TagAdminDTO> tags = tagMapper.listTagsAdmin(PageUtil.getLimitCurrent(), PageUtil.getSize(), conditionVO);
        return new PageResult<>(tags, count);
    }

    @SneakyThrows
    @Override
    public List<TagAdminDTO> listTagsAdminBySearch(ConditionVO conditionVO) {
        List<Tag> tagList = tagMapper.selectList(new LambdaQueryWrapper<Tag>()
                .like(StringUtils.isNotBlank(conditionVO.getKeywords()), Tag::getTagName, conditionVO.getKeywords())
                .orderByDesc(Tag::getId));
        return BeanCopyUtil.copyList(tagList, TagAdminDTO.class);
    }

    @Override
    public void saveOrUpdateTag(TagVO tagVO) {
        Tag existTag = tagMapper.selectOne(new LambdaQueryWrapper<Tag>()
                .select(Tag::getId)
                .eq(Tag::getTagName, tagVO.getTagName()));
        if (Objects.nonNull(existTag) && !existTag.getId().equals(tagVO.getId())) {
            throw new BizException("标签名已存在");
        }
        Tag tag = BeanCopyUtil.copyObject(tagVO, Tag.class);
        this.saveOrUpdate(tag);
    }

    @Override
    public void deleteTag(List<Integer> tagIds) {
        // 查询标签下是否有文章
        Integer count = articleTagMapper.selectCount(new LambdaQueryWrapper<ArticleTag>()
                .in(ArticleTag::getTagId, tagIds));
        if (count > 0) {
            throw new BizException("删除失败，该标签下存在文章");
        }
        tagMapper.deleteBatchIds(tagIds);
    }
}

