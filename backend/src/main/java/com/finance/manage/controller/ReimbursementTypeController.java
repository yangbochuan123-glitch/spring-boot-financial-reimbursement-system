package com.finance.manage.controller;

import com.finance.manage.common.Result;
import com.finance.manage.entity.ReimbursementType;
import com.finance.manage.service.ReimbursementTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 报销单类型
 */
@RestController
@RequestMapping("/api/reimbursement/type")
public class ReimbursementTypeController {

    @Autowired
    private ReimbursementTypeService reimbursementTypeService;

    @GetMapping("/list")
    public Result<?> getList() {
        return reimbursementTypeService.getList();
    }

    @PostMapping("/add")
    public Result<?> add(@RequestBody ReimbursementType type) {
        return reimbursementTypeService.add(type);
    }

    @PostMapping("/update")
    public Result<?> update(@RequestBody ReimbursementType type) {
        return reimbursementTypeService.update(type);
    }

    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Long id) {
        return reimbursementTypeService.delete(id);
    }
} 