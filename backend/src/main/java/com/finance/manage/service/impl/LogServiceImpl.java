package com.finance.manage.service.impl;

import com.finance.manage.common.Result;
import com.finance.manage.entity.SysLog;
import com.finance.manage.mapper.LogMapper;
import com.finance.manage.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogMapper logMapper;

    @Override
    public Result<?> getList(String username, String operation) {
        List<SysLog> list = logMapper.selectList(username, operation);
        return Result.success(list);
    }

    @Override
    public Result<?> add(SysLog log) {
        int rows = logMapper.insert(log);
        return rows > 0 ? Result.success() : Result.error("添加失败");
    }

    @Override
    public Result<?> delete(Long id) {
        int rows = logMapper.delete(id);
        return rows > 0 ? Result.success() : Result.error("删除失败");
    }

    @Override
    public Result<?> clear() {
        int rows = logMapper.clear();
        return Result.success();
    }
} 