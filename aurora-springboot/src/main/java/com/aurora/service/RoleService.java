package com.aurora.service;

import com.aurora.model.dto.RoleDTO;
import com.aurora.model.dto.UserRoleDTO;
import com.aurora.entity.Role;
import com.aurora.model.vo.ConditionVO;
import com.aurora.model.dto.PageResultDTO;
import com.aurora.model.vo.RoleVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface RoleService extends IService<Role> {

    List<UserRoleDTO> listUserRoles();

    PageResultDTO<RoleDTO> listRoles(ConditionVO conditionVO);

    void saveOrUpdateRole(RoleVO roleVO);

    void deleteRoles(List<Integer> ids);

}
