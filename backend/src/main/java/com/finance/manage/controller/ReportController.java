package com.finance.manage.controller;

import com.finance.manage.common.Result;
import com.finance.manage.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 报表的统计
 */
@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/reimbursement-stats")
    public Result<?> getReimbursementStats() {
        return reportService.getReimbursementStats();
    }

    @GetMapping("/type-stats")
    public Result<?> getTypeStats() {
        return reportService.getTypeStats();
    }

    @GetMapping("/monthly-stats")
    public Result<?> getMonthlyStats(@RequestParam Integer year) {
        return reportService.getMonthlyStats(year);
    }
} 