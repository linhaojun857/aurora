package com.aurora.service;

import com.aurora.dto.TagAdminDTO;
import com.aurora.dto.TagDTO;
import com.aurora.entity.Tag;
import com.aurora.vo.ConditionVO;
import com.aurora.vo.PageResult;
import com.aurora.vo.TagVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface TagService extends IService<Tag> {

    List<TagDTO> listTags();

    List<TagDTO> listTopTenTags();

    PageResult<TagAdminDTO> listTagsAdmin(ConditionVO conditionVO);

    List<TagAdminDTO> listTagsAdminBySearch(ConditionVO conditionVO);

    void saveOrUpdateTag(TagVO tagVO);

    void deleteTag(List<Integer> tagIds);
}
