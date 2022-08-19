package com.aurora.service;

import com.aurora.dto.LabelOptionDTO;
import com.aurora.dto.MenuDTO;
import com.aurora.dto.UserMenuDTO;
import com.aurora.entity.Menu;
import com.aurora.vo.ConditionVO;
import com.aurora.vo.IsHiddenVO;
import com.aurora.vo.MenuVO;
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
