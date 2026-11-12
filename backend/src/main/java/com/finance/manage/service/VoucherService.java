package com.finance.manage.service;

import com.finance.manage.common.Result;
import com.finance.manage.entity.Voucher;

public interface VoucherService {
    Result<?> getList(Integer type, String voucherNo);
    Result<?> add(Voucher voucher);
    Result<?> update(Voucher voucher);
    Result<?> delete(Long id);
    Result<?> getDetail(Long id);
    Result<?> getStatistics();
} 