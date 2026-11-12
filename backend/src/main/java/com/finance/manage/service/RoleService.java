package com.finance.manage.service;

import com.finance.manage.common.Result;
import com.finance.manage.entity.SysRole;

public interface RoleService {
    Result<?> getList();
    Result<?> add(SysRole role);
    Result<?> update(SysRole role);
    Result<?> delete(Long id);
    Result<?> getDetail(Long id);
} 