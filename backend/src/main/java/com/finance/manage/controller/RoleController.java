package com.finance.manage.controller;

import com.finance.manage.common.Result;
import com.finance.manage.entity.SysRole;
import com.finance.manage.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 角色管理，做角色的增删查改的操作
 */
@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/list")
    public Result<?> getList() {
        return roleService.getList();
    }

    @PostMapping("/add")
    public Result<?> add(@RequestBody SysRole role) {
        return roleService.add(role);
    }

    @PutMapping("/update")
    public Result<?> update(@RequestBody SysRole role) {
        return roleService.update(role);
    }

    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Long id) {
        return roleService.delete(id);
    }

    @GetMapping("/detail/{id}")
    public Result<?> getDetail(@PathVariable Long id) {
        return roleService.getDetail(id);
    }
} 