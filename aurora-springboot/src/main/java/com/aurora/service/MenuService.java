package com.aurora.service;

import com.aurora.model.dto.LabelOptionDTO;
import com.aurora.model.dto.MenuDTO;
import com.aurora.model.dto.UserMenuDTO;
import com.aurora.entity.Menu;
import com.aurora.model.vo.ConditionVO;
import com.aurora.model.vo.IsHiddenVO;
import com.aurora.model.vo.MenuVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface MenuService extends IService<Menu> {

    List<MenuDTO> listMenus(ConditionVO conditionVO);

    void saveOrUpdateMenu(MenuVO menuVO);

    void updateMenuIsHidden(IsHiddenVO isHiddenVO);

    void deleteMenu(Integer menuId);

    List<LabelOptionDTO> listMenuOptions();

    List<UserMenuDTO> listUserMenus();

}
