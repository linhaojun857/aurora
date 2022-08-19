package com.aurora.mapper;

import com.aurora.dto.CategoryAdminDTO;
import com.aurora.dto.CategoryDTO;
import com.aurora.entity.Category;
import com.aurora.vo.ConditionVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CategoryMapper extends BaseMapper<Category> {

    List<CategoryDTO> listCategories();

    List<CategoryAdminDTO> listCategoriesAdmin(@Param("current") Long current, @Param("size") Long size, @Param("conditionVO") ConditionVO conditionVO);
}
