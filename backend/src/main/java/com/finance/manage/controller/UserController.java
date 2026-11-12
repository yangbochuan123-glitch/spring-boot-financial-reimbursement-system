package com.finance.manage.controller;

import com.finance.manage.common.Result;
import com.finance.manage.entity.SysUser;
import com.finance.manage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * y用户管理的controller
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public Result<?> getList(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Long roleId) {
        return userService.getList(username, roleId);
    }

    @PostMapping("/add")
    public Result<?> add(@RequestBody SysUser user) {
        return userService.add(user);
    }

    @PostMapping("/update")
    public Result<?> update(@RequestBody SysUser user) {
        return userService.update(user);
    }

    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Long id) {
        return userService.delete(id);
    }

    /**
     * 设置/修改密码
     * @param id
     * @return
     */
    @PostMapping("/reset-password/{id}")
    public Result<?> resetPassword(@PathVariable Long id) {
        return userService.resetPassword(id);
    }

    /**
     * 更新密码
     * @param params
     * @return
     */
    @PostMapping("/update-password")
    public Result<?> updatePassword(@RequestBody Map<String, String> params) {
        Long userId = Long.parseLong(params.get("userId"));
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");
        return userService.updatePassword(userId, oldPassword, newPassword);
    }

    /**
     * 查询某一个用户的具体信息
     * @param id
     * @return
     */
    @GetMapping("/detail/{id}")
    public Result<?> getDetail(@PathVariable Long id) {
        return userService.getDetail(id);
    }
} 