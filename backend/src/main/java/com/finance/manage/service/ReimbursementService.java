package com.finance.manage.service;

import com.finance.manage.common.Result;
import com.finance.manage.entity.Reimbursement;

public interface ReimbursementService {
    Result<?> apply(Reimbursement reimbursement);
    Result<?> getList(Long applicantId, Integer status);
    Result<?> approve(Long id, Integer status);
    Result<?> getDetail(Long id);

    /**
     * 审批报销申请
     * @param id 报销单ID
     * @param status 审批状态
     * @param comment 审批意见
     * @param approverId 审批人ID
     * @return 审批结果
     */
    Result approve(Long id, Integer status, String comment, Long approverId);

    /**
     * 获取报销单审批意见
     * @param id 报销单ID
     * @return 审批意见列表
     */
    Result getApprovalComments(Long id);

    /**
     * 重新提交报销申请
     * @param id 报销单ID
     * @return 提交结果
     */
    Result resubmit(Long id);
} 