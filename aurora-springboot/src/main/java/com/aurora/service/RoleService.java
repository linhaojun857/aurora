package com.aurora.service;

import com.aurora.dto.RoleDTO;
import com.aurora.dto.UserRoleDTO;
import com.aurora.entity.Role;
import com.aurora.vo.ConditionVO;
import com.aurora.vo.PageResult;
import com.aurora.vo.RoleVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface RoleService extends IService<Role> {

    List<UserRoleDTO> listUserRoles();

    PageResult<RoleDTO> listRoles(ConditionVO conditionVO);

    void saveOrUpdateRole(RoleVO roleVO);

    void deleteRoles(List<Integer> ids);
}
