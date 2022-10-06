package com.aurora.controller;

import com.aurora.annotation.OptLog;
import com.aurora.model.dto.LabelOptionDTO;
import com.aurora.model.dto.MenuDTO;
import com.aurora.model.dto.UserMenuDTO;
import com.aurora.model.vo.ResultVO;
import com.aurora.service.MenuService;
import com.aurora.model.vo.ConditionVO;
import com.aurora.model.vo.IsHiddenVO;
import com.aurora.model.vo.MenuVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.aurora.constant.OptTypeConstant.*;

@Api(tags = "菜单模块")
@RestController
public class MenuController {

    @Autowired
    private MenuService menuService;

    @ApiOperation(value = "查看菜单列表")
    @GetMapping("/admin/menus")
    public ResultVO<List<MenuDTO>> listMenus(ConditionVO conditionVO) {
        return ResultVO.ok(menuService.listMenus(conditionVO));
    }

    @OptLog(optType =SAVE_OR_UPDATE)
    @ApiOperation(value = "新增或修改菜单")
    @PostMapping("/admin/menus")
    public ResultVO<?> saveOrUpdateMenu(@Valid @RequestBody MenuVO menuVO) {
        menuService.saveOrUpdateMenu(menuVO);
        return ResultVO.ok();
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改目录是否隐藏")
    @PutMapping("/admin/menus/isHidden")
    public ResultVO<?> updateMenuIsHidden(@RequestBody IsHiddenVO isHiddenVO) {
        menuService.updateMenuIsHidden(isHiddenVO);
        return ResultVO.ok();
    }

    @OptLog(optType = DELETE)
    @ApiOperation(value = "删除菜单")
    @DeleteMapping("/admin/menus/{menuId}")
    public ResultVO<?> deleteMenu(@PathVariable("menuId") Integer menuId) {
        menuService.deleteMenu(menuId);
        return ResultVO.ok();
    }

    @ApiOperation(value = "查看角色菜单选项")
    @GetMapping("/admin/role/menus")
    public ResultVO<List<LabelOptionDTO>> listMenuOptions() {
        return ResultVO.ok(menuService.listMenuOptions());
    }

    @ApiOperation(value = "查看当前用户菜单")
    @GetMapping("/admin/user/menus")
    public ResultVO<List<UserMenuDTO>> listUserMenus() {
        return ResultVO.ok(menuService.listUserMenus());
    }
}
