package com.aurora.controller;

import com.aurora.annotation.OptLog;
import com.aurora.model.dto.RoleDTO;
import com.aurora.model.dto.UserRoleDTO;
import com.aurora.model.vo.ResultVO;
import com.aurora.service.RoleService;
import com.aurora.model.vo.ConditionVO;
import com.aurora.model.dto.PageResultDTO;
import com.aurora.model.vo.RoleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.aurora.constant.OptTypeConstant.*;

@Api(tags = "角色模块")
@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "查询用户角色选项")
    @GetMapping("/admin/users/role")
    public ResultVO<List<UserRoleDTO>> listUserRoles() {
        return ResultVO.ok(roleService.listUserRoles());
    }


    @ApiOperation(value = "查询角色列表")
    @GetMapping("/admin/roles")
    public ResultVO<PageResultDTO<RoleDTO>> listRoles(ConditionVO conditionVO) {
        return ResultVO.ok(roleService.listRoles(conditionVO));
    }

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "保存或更新角色")
    @PostMapping("/admin/role")
    public ResultVO<?> saveOrUpdateRole(@RequestBody @Valid RoleVO roleVO) {
        roleService.saveOrUpdateRole(roleVO);
        return ResultVO.ok();
    }

    @OptLog(optType = DELETE)
    @ApiOperation(value = "删除角色")
    @DeleteMapping("/admin/roles")
    public ResultVO<?> deleteRoles(@RequestBody List<Integer> roleIdList) {
        roleService.deleteRoles(roleIdList);
        return ResultVO.ok();
    }
}
