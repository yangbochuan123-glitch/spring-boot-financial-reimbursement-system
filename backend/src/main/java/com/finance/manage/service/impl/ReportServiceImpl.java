package com.finance.manage.service.impl;

import com.finance.manage.common.Result;
import com.finance.manage.mapper.ReimbursementMapper;
import com.finance.manage.mapper.VoucherMapper;
import com.finance.manage.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReimbursementMapper reimbursementMapper;

    @Autowired
    private VoucherMapper voucherMapper;

    @Override
    public Result<?> getReimbursementStats() {
        //根据报表的状态进行统计
        List<Map<String, Object>> stats = reimbursementMapper.selectStatsByStatus();
        return Result.success(stats);
    }

    @Override
    public Result<?> getTypeStats() {
        List<Map<String, Object>> stats = reimbursementMapper.selectStatsByType();
        return Result.success(stats);
    }

    @Override
    public Result<?> getMonthlyStats(Integer year) {
        List<Map<String, Object>> monthlyStats = new ArrayList<>();
        
        // 获取每月的收支数据
        List<Map<String, Object>> incomeStats = voucherMapper.selectMonthlyStats(year, 1);
        List<Map<String, Object>> expenseStats = voucherMapper.selectMonthlyStats(year, 2);
        
        // 将收支数据按月整理
        Map<Integer, BigDecimal> incomeMap = new HashMap<>();
        Map<Integer, BigDecimal> expenseMap = new HashMap<>();
        
        for (Map<String, Object> stat : incomeStats) {
            incomeMap.put((Integer) stat.get("month"), (BigDecimal) stat.get("amount"));
        }
        
        for (Map<String, Object> stat : expenseStats) {
            expenseMap.put((Integer) stat.get("month"), (BigDecimal) stat.get("amount"));
        }
        
        // 生成1-12月的统计数据
        for (int month = 1; month <= 12; month++) {
            Map<String, Object> monthlyStat = new HashMap<>();
            monthlyStat.put("month", month);
            
            BigDecimal income = incomeMap.getOrDefault(month, BigDecimal.ZERO);
            BigDecimal expense = expenseMap.getOrDefault(month, BigDecimal.ZERO);
            BigDecimal balance = income.subtract(expense);
            
            monthlyStat.put("income", income);
            monthlyStat.put("expense", expense);
            monthlyStat.put("balance", balance);
            
            monthlyStats.add(monthlyStat);
        }
        
        return Result.success(monthlyStats);
    }
} 