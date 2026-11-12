package com.finance.manage.service.impl;

import com.finance.manage.common.Result;
import com.finance.manage.entity.SysRole;
import com.finance.manage.mapper.RoleMapper;
import com.finance.manage.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Result<?> getList() {
        List<SysRole> list = roleMapper.selectList();
        return Result.success(list);
    }

    @Override
    public Result<?> add(SysRole role) {
        int rows = roleMapper.insert(role);
        return rows > 0 ? Result.success() : Result.error("添加失败");
    }

    @Override
    public Result<?> update(SysRole role) {
        int rows = roleMapper.update(role);
        return rows > 0 ? Result.success() : Result.error("更新失败");
    }

    @Override
    public Result<?> delete(Long id) {
        // Check if role has associated users
        int count = roleMapper.countUsersByRoleId(id);
        if (count > 0) {
            return Result.error("该角色下存在用户，无法删除");
        }
        int rows = roleMapper.delete(id);
        return rows > 0 ? Result.success() : Result.error("删除失败");
    }

    @Override
    public Result<?> getDetail(Long id) {
        SysRole role = roleMapper.selectById(id);
        return role != null ? Result.success(role) : Result.error("角色不存在");
    }
} 