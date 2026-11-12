package com.finance.manage.controller;

import com.finance.manage.common.Result;
import com.finance.manage.entity.Reimbursement;
import com.finance.manage.service.ReimbursementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reimbursement")
public class ReimbursementController {

    @Autowired
    private ReimbursementService reimbursementService;

    @PostMapping("/apply")
    public Result<?> apply(@RequestBody Reimbursement reimbursement) {
        return reimbursementService.apply(reimbursement);
    }

    @GetMapping("/list")
    public Result<?> getList(
            @RequestParam(required = false) Long applicantId,
            @RequestParam(required = false) Integer status) {
        return reimbursementService.getList(applicantId, status);
    }

    @PutMapping("/approve/{id}")
    public Result<?> approve(@PathVariable Long id, @RequestParam Integer status,
                           @RequestParam(required = false) String comment,
                           @RequestParam Long userId) {
        if (comment != null && !comment.isEmpty()) {
            return reimbursementService.approve(id, status, comment, userId);
        } else {
            return reimbursementService.approve(id, status);
        }
    }

    @GetMapping("/detail/{id}")
    public Result<?> getDetail(@PathVariable Long id) {
        return reimbursementService.getDetail(id);
    }

    @GetMapping("/approval-comments/{id}")
    public Result getApprovalComments(@PathVariable Long id) {
        return reimbursementService.getApprovalComments(id);
    }

    @PutMapping("/resubmit/{id}")
    public Result resubmit(@PathVariable Long id) {
        return reimbursementService.resubmit(id);
    }
} 