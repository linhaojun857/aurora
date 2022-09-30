package com.aurora.service.impl;

import com.aurora.model.dto.LabelOptionDTO;
import com.aurora.model.dto.MenuDTO;
import com.aurora.model.dto.UserMenuDTO;
import com.aurora.entity.Menu;
import com.aurora.entity.RoleMenu;
import com.aurora.exception.BizException;
import com.aurora.mapper.MenuMapper;
import com.aurora.mapper.RoleMenuMapper;
import com.aurora.service.MenuService;
import com.aurora.util.BeanCopyUtil;
import com.aurora.util.UserUtil;
import com.aurora.model.vo.ConditionVO;
import com.aurora.model.vo.IsHiddenVO;
import com.aurora.model.vo.MenuVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.aurora.constant.CommonConstant.COMPONENT;
import static com.aurora.constant.CommonConstant.TRUE;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Override
    public List<MenuDTO> listMenus(ConditionVO conditionVO) {
        // 查询菜单数据
        List<Menu> menus = menuMapper.selectList(new LambdaQueryWrapper<Menu>()
                .like(StringUtils.isNotBlank(conditionVO.getKeywords()), Menu::getName, conditionVO.getKeywords()));
        // 获取目录列表
        List<Menu> catalogs = listCatalogs(menus);
        // 获取目录下的子菜单
        Map<Integer, List<Menu>> childrenMap = getMenuMap(menus);
        // 组装目录菜单数据
        List<MenuDTO> menuDTOs = catalogs.stream().map(item -> {
            MenuDTO menuDTO = BeanCopyUtil.copyObject(item, MenuDTO.class);
            // 获取目录下的菜单排序
            List<MenuDTO> list = BeanCopyUtil.copyList(childrenMap.get(item.getId()), MenuDTO.class).stream()
                    .sorted(Comparator.comparing(MenuDTO::getOrderNum))
                    .collect(Collectors.toList());
            menuDTO.setChildren(list);
            childrenMap.remove(item.getId());
            return menuDTO;
        }).sorted(Comparator.comparing(MenuDTO::getOrderNum)).collect(Collectors.toList());
        // 若还有菜单未取出则拼接
        if (CollectionUtils.isNotEmpty(childrenMap)) {
            List<Menu> childrenList = new ArrayList<>();
            childrenMap.values().forEach(childrenList::addAll);
            List<MenuDTO> childrenDTOList = childrenList.stream()
                    .map(item -> BeanCopyUtil.copyObject(item, MenuDTO.class))
                    .sorted(Comparator.comparing(MenuDTO::getOrderNum))
                    .collect(Collectors.toList());
            menuDTOs.addAll(childrenDTOList);
        }
        return menuDTOs;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateMenu(MenuVO menuVO) {
        Menu menu = BeanCopyUtil.copyObject(menuVO, Menu.class);
        this.saveOrUpdate(menu);
    }

    @Override
    public void updateMenuIsHidden(IsHiddenVO isHiddenVO) {
        Menu menu = BeanCopyUtil.copyObject(isHiddenVO, Menu.class);
        menuMapper.updateById(menu);
    }

    @Override
    public void deleteMenu(Integer menuId) {
        // 查询是否有角色关联
        Integer count = roleMenuMapper.selectCount(new LambdaQueryWrapper<RoleMenu>()
                .eq(RoleMenu::getMenuId, menuId));
        if (count > 0) {
            throw new BizException("菜单下有角色关联");
        }
        // 查询子菜单
        List<Integer> menuIds = menuMapper.selectList(new LambdaQueryWrapper<Menu>()
                        .select(Menu::getId)
                        .eq(Menu::getParentId, menuId))
                .stream()
                .map(Menu::getId)
                .collect(Collectors.toList());
        menuIds.add(menuId);
        menuMapper.deleteBatchIds(menuIds);
    }

    @Override
    public List<LabelOptionDTO> listMenuOptions() {
        // 查询菜单数据
        List<Menu> menus = menuMapper.selectList(new LambdaQueryWrapper<Menu>()
                .select(Menu::getId, Menu::getName, Menu::getParentId, Menu::getOrderNum));
        // 获取目录列表
        List<Menu> catalogs = listCatalogs(menus);
        // 获取目录下的子菜单
        Map<Integer, List<Menu>> childrenMap = getMenuMap(menus);
        // 组装目录菜单数据
        return catalogs.stream().map(item -> {
            // 获取目录下的菜单排序
            List<LabelOptionDTO> list = new ArrayList<>();
            List<Menu> children = childrenMap.get(item.getId());
            if (CollectionUtils.isNotEmpty(children)) {
                list = children.stream()
                        .sorted(Comparator.comparing(Menu::getOrderNum))
                        .map(menu -> LabelOptionDTO.builder()
                                .id(menu.getId())
                                .label(menu.getName())
                                .build())
                        .collect(Collectors.toList());
            }
            return LabelOptionDTO.builder()
                    .id(item.getId())
                    .label(item.getName())
                    .children(list)
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public List<UserMenuDTO> listUserMenus() {
        // 查询用户菜单信息
        List<Menu> menus = menuMapper.listMenusByUserInfoId(UserUtil.getUserDetailsDTO().getUserInfoId());
        // 获取目录列表
        List<Menu> catalogs = listCatalogs(menus);
        // 获取目录下的子菜单
        Map<Integer, List<Menu>> childrenMap = getMenuMap(menus);
        // 转换前端菜单格式
        return convertUserMenuList(catalogs, childrenMap);
    }

    private List<Menu> listCatalogs(List<Menu> menus) {
        return menus.stream()
                .filter(item -> Objects.isNull(item.getParentId()))
                .sorted(Comparator.comparing(Menu::getOrderNum))
                .collect(Collectors.toList());
    }

    private Map<Integer, List<Menu>> getMenuMap(List<Menu> menus) {
        return menus.stream()
                .filter(item -> Objects.nonNull(item.getParentId()))
                .collect(Collectors.groupingBy(Menu::getParentId));
    }

    private List<UserMenuDTO> convertUserMenuList(List<Menu> catalogList, Map<Integer, List<Menu>> childrenMap) {
        return catalogList.stream().map(item -> {
            // 获取目录
            UserMenuDTO userMenuDTO = new UserMenuDTO();
            List<UserMenuDTO> list = new ArrayList<>();
            // 获取目录下的子菜单
            List<Menu> children = childrenMap.get(item.getId());
            if (CollectionUtils.isNotEmpty(children)) {
                // 多级菜单处理
                userMenuDTO = BeanCopyUtil.copyObject(item, UserMenuDTO.class);
                list = children.stream()
                        .sorted(Comparator.comparing(Menu::getOrderNum))
                        .map(menu -> {
                            UserMenuDTO dto = BeanCopyUtil.copyObject(menu, UserMenuDTO.class);
                            dto.setHidden(menu.getIsHidden().equals(TRUE));
                            return dto;
                        })
                        .collect(Collectors.toList());
            } else {
                // 一级菜单处理
                userMenuDTO.setPath(item.getPath());
                userMenuDTO.setComponent(COMPONENT);
                list.add(UserMenuDTO.builder()
                        .path("")
                        .name(item.getName())
                        .icon(item.getIcon())
                        .component(item.getComponent())
                        .build());
            }
            userMenuDTO.setHidden(item.getIsHidden().equals(TRUE));
            userMenuDTO.setChildren(list);
            return userMenuDTO;
        }).collect(Collectors.toList());
    }
}
