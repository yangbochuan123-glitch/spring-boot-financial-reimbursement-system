package com.finance.manage.service;

import com.finance.manage.common.Result;
import com.finance.manage.entity.SysLog;

public interface LogService {
    Result<?> getList(String username, String operation);
    Result<?> add(SysLog log);
    Result<?> delete(Long id);
    Result<?> clear();
} 