package com.finance.manage.service;

import com.finance.manage.common.Result;

public interface ReportService {
    Result<?> getReimbursementStats();
    Result<?> getTypeStats();
    Result<?> getMonthlyStats(Integer year);
} 