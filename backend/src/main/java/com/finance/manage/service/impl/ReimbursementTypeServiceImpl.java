package com.finance.manage.service.impl;

import com.finance.manage.common.Result;
import com.finance.manage.entity.ReimbursementType;
import com.finance.manage.mapper.ReimbursementTypeMapper;
import com.finance.manage.service.ReimbursementTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReimbursementTypeServiceImpl implements ReimbursementTypeService {

    @Autowired
    private ReimbursementTypeMapper reimbursementTypeMapper;

    @Override
    public Result<?> getList() {
        List<ReimbursementType> list = reimbursementTypeMapper.selectList();
        return Result.success(list);
    }

    @Override
    public Result<?> add(ReimbursementType type) {
        int rows = reimbursementTypeMapper.insert(type);
        return rows > 0 ? Result.success() : Result.error("添加失败");
    }

    @Override
    public Result<?> update(ReimbursementType type) {
        int rows = reimbursementTypeMapper.update(type);
        return rows > 0 ? Result.success() : Result.error("更新失败");
    }

    @Override
    public Result<?> delete(Long id) {
        int rows = reimbursementTypeMapper.delete(id);
        return rows > 0 ? Result.success() : Result.error("删除失败");
    }
} 