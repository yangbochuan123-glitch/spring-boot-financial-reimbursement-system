package com.finance.manage.service;

import com.finance.manage.common.Result;
import com.finance.manage.entity.SysUser;

public interface UserService {
    Result<?> login(SysUser user);
    Result<?> getList(String username, Long roleId);
    Result<?> add(SysUser user);
    Result<?> update(SysUser user);
    Result<?> delete(Long id);
    Result<?> resetPassword(Long id);
    Result<?> updatePassword(Long id, String oldPassword, String newPassword);
    Result<?> getDetail(Long id);
} 