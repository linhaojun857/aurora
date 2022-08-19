package com.aurora.service;

import com.aurora.dto.CategoryAdminDTO;
import com.aurora.dto.CategoryDTO;
import com.aurora.dto.CategoryOptionDTO;
import com.aurora.entity.Category;
import com.aurora.vo.CategoryVO;
import com.aurora.vo.ConditionVO;
import com.aurora.vo.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface CategoryService extends IService<Category> {

    List<CategoryDTO> listCategories();

    PageResult<CategoryAdminDTO> listCategoriesAdmin(ConditionVO conditionVO);

    List<CategoryOptionDTO> listCategoriesBySearch(ConditionVO conditionVO);

    void deleteCategories(List<Integer> categoryIds);

    void saveOrUpdateCategory(CategoryVO categoryVO);
}
