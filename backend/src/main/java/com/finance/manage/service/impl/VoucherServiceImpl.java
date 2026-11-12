package com.finance.manage.service.impl;

import com.finance.manage.common.Result;
import com.finance.manage.entity.Voucher;
import com.finance.manage.mapper.VoucherMapper;
import com.finance.manage.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VoucherServiceImpl implements VoucherService {
    @Autowired
    private VoucherMapper voucherMapper;

    @Override
    public Result<?> getList(Integer type, String voucherNo) {
        List<Voucher> list = voucherMapper.selectList(type, voucherNo);
        return Result.success(list);
    }

    @Override
    public Result<?> add(Voucher voucher) {
        voucher.setCreateTime(LocalDateTime.now());
        voucherMapper.insert(voucher);
        return Result.success();
    }

    @Override
    public Result<?> update(Voucher voucher) {
        voucherMapper.update(voucher);
        return Result.success();
    }

    @Override
    public Result<?> delete(Long id) {
        voucherMapper.delete(id);
        return Result.success();
    }

    @Override
    public Result<?> getDetail(Long id) {
        Voucher voucher = voucherMapper.selectById(id);
        return Result.success(voucher);
    }

    @Override
    public Result<?> getStatistics() {
        Map<String, BigDecimal> statistics = new HashMap<>();
        // 收入统计
        BigDecimal income = voucherMapper.sumAmountByType(1);
        statistics.put("income", income == null ? BigDecimal.ZERO : income);
        // 支出统计
        BigDecimal expense = voucherMapper.sumAmountByType(2);
        statistics.put("expense", expense == null ? BigDecimal.ZERO : expense);
        return Result.success(statistics);
    }
} 