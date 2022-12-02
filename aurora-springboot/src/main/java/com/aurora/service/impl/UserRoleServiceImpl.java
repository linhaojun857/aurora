package com.aurora.service.impl;

import com.aurora.entity.UserRole;
import com.aurora.mapper.UserRoleMapper;
import com.aurora.service.UserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}
