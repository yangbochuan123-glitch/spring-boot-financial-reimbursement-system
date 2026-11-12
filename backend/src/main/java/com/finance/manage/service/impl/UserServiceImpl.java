package com.finance.manage.service.impl;

import com.finance.manage.common.Result;
import com.finance.manage.entity.SysUser;
import com.finance.manage.mapper.UserMapper;
import com.finance.manage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    private static final String DEFAULT_PASSWORD = "123456";

    @Override
    public Result<?> login(SysUser user) {
        SysUser dbUser = userMapper.findByUsername(user.getUsername());
        if (dbUser == null) {
            return Result.error("用户不存在");
        }
        if (!dbUser.getPassword().equals(user.getPassword())) {
            return Result.error("密码错误");
        }
        //校验用户的状态 如果被仅有，也不允许登录
        if (dbUser.getStatus() == 0) {
            return Result.error("账号已被禁用");
        }
        // Clear password before returning
        dbUser.setPassword(null);
        return Result.success(dbUser);
    }

    @Override
    public Result<?> getList(String username, Long roleId) {
        List<SysUser> list = userMapper.selectList(username, roleId);
        // Clear passwords
        list.forEach(user -> user.setPassword(null));
        return Result.success(list);
    }

    @Override
    public Result<?> add(SysUser user) {
        // Check if username exists
        SysUser existUser = userMapper.findByUsername(user.getUsername());
        if (existUser != null) {
            return Result.error("用户名已存在");
        }

        // Set default password
        user.setPassword(DEFAULT_PASSWORD);
        user.setStatus(1); // Enable by default

        int rows = userMapper.insert(user);
        return rows > 0 ? Result.success() : Result.error("添加失败");
    }

    @Override
    public Result<?> update(SysUser user) {
        int rows = userMapper.update(user);
        return rows > 0 ? Result.success() : Result.error("更新失败");
    }

    @Override
    public Result<?> delete(Long id) {
        int rows = userMapper.delete(id);
        return rows > 0 ? Result.success() : Result.error("删除失败");
    }

    @Override
    public Result<?> resetPassword(Long id) {
        int rows = userMapper.updatePassword(id, DEFAULT_PASSWORD);
        return rows > 0 ? Result.success() : Result.error("重置密码失败");
    }

    @Override
    public Result<?> updatePassword(Long id, String oldPassword, String newPassword) {
        SysUser user = userMapper.selectById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        if (!user.getPassword().equals(oldPassword)) {
            return Result.error("原密码错误");
        }

        int rows = userMapper.updatePassword(id, newPassword);
        return rows > 0 ? Result.success() : Result.error("修改密码失败");
    }

    @Override
    public Result<?> getDetail(Long id) {
        SysUser user = userMapper.selectById(id);
        if (user != null) {
            user.setPassword(null);
        }
        return user != null ? Result.success(user) : Result.error("用户不存在");
    }
} 