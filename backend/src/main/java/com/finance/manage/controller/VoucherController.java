package com.finance.manage.controller;

import com.finance.manage.common.Result;
import com.finance.manage.entity.Voucher;
import com.finance.manage.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 凭证的增删查改管理
 */
@RestController
@RequestMapping("/api/voucher")
public class VoucherController {
    @Autowired
    private VoucherService voucherService;

    @GetMapping("/list")
    public Result<?> getList(@RequestParam(required = false) Integer type,
                           @RequestParam(required = false) String voucherNo) {
        return voucherService.getList(type, voucherNo);
    }

    @PostMapping("/add")
    public Result<?> add(@RequestBody Voucher voucher) {
        return voucherService.add(voucher);
    }

    @PostMapping("/update")
    public Result<?> update(@RequestBody Voucher voucher) {
        return voucherService.update(voucher);
    }

    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Long id) {
        return voucherService.delete(id);
    }

    @GetMapping("/detail/{id}")
    public Result<?> getDetail(@PathVariable Long id) {
        return voucherService.getDetail(id);
    }

    @GetMapping("/statistics")
    public Result<?> getStatistics() {
        return voucherService.getStatistics();
    }
} 