package com.finance.manage.controller;

import com.finance.manage.common.Result;
import com.finance.manage.entity.SysUser;
import com.finance.manage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<?> login(@RequestBody SysUser user) {
        return userService.login(user);
    }

    @PostMapping("/logout")
    public Result<?> logout() {
        return Result.success("退出成功");
    }
} 