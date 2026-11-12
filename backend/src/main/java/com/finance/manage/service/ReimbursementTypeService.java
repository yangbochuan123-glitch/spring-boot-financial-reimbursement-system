package com.finance.manage.service;

import com.finance.manage.common.Result;
import com.finance.manage.entity.ReimbursementType;

public interface ReimbursementTypeService {
    Result<?> getList();
    Result<?> add(ReimbursementType type);
    Result<?> update(ReimbursementType type);
    Result<?> delete(Long id);
} 