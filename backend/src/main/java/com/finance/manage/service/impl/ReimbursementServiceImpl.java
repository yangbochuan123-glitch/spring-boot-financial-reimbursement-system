package com.finance.manage.service.impl;

import com.finance.manage.common.Result;
import com.finance.manage.entity.Reimbursement;
import com.finance.manage.entity.ReimbursementApproval;
import com.finance.manage.entity.ReimbursementType;
import com.finance.manage.entity.Voucher;
import com.finance.manage.mapper.*;
import com.finance.manage.service.ReimbursementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReimbursementServiceImpl implements ReimbursementService {

    @Autowired
    private ReimbursementMapper reimbursementMapper;

    @Autowired
    private ApprovalMapper approvalMapper;

    @Autowired
    private VoucherMapper voucherMapper;

    @Autowired
    private ReimbursementTypeMapper reimbursementTypeMapper;

    @Override
    @Transactional
    public Result<?> apply(Reimbursement reimbursement) {
        // 获取报销类型信息
        ReimbursementType type = reimbursementTypeMapper.selectById(reimbursement.getTypeId());
        if (type == null) {
            return Result.error("报销类型不存在");
        }

        // 检查报销金额是否超过限额
        if (type.getBudgetLimit() != null && reimbursement.getAmount().compareTo(type.getBudgetLimit()) > 0) {
            return Result.error("报销金额不能超过类型限额：¥" + type.getBudgetLimit());
        }

        reimbursement.setStatus(0); // 设置初始状态为待审核
        int rows = reimbursementMapper.insert(reimbursement);
        return rows > 0 ? Result.success() : Result.error("申请失败");
    }

    @Override
    public Result<?> getList(Long applicantId, Integer status) {
        List<Reimbursement> list = reimbursementMapper.selectList(applicantId, status);
        return Result.success(list);
    }

    @Override
    public Result<?> approve(Long id, Integer status) {
        int rows = reimbursementMapper.updateStatus(id, status);
        return rows > 0 ? Result.success() : Result.error("审核失败");
    }

    @Override
    @Transactional
    public Result approve(Long id, Integer status, String comment, Long approverId) {
        // 检查报销单是否存在
        Reimbursement reimbursement = reimbursementMapper.selectById(id);
        if (reimbursement == null) {
            return Result.error("报销单不存在");
        }

        // 检查状态是否合法
        if (status != 1 && status != 2) {
            return Result.error("非法的审批状态");
        }

        // 更新报销单状态
        int rows = reimbursementMapper.updateStatus(id, status);
        if (rows == 0) {
            return Result.error("更新报销单状态失败");
        }

        // 保存审批意见
        ReimbursementApproval approval = new ReimbursementApproval();
        approval.setReimbursementId(id);
        approval.setApproverId(approverId);
        approval.setStatus(status);
        approval.setComment(comment);
        approvalMapper.insert(approval);

        // 如果审批通过，创建凭证记录
        if (status == 1) {
            Voucher voucher = new Voucher();
            // 生成凭证编号：VOU + 年月日 + 4位序号
            String voucherNo = "VOU" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                    + String.format("%04d", reimbursement.getId());
            voucher.setVoucherNo(voucherNo);
            voucher.setType(2); // 2-支出
            voucher.setAmount(reimbursement.getAmount());
            voucher.setDescription("报销支出-" + reimbursement.getTypeName());
            voucher.setOperatorId(approverId);
            voucherMapper.insert(voucher);
        }

        return Result.success();
    }

    @Override
    public Result getApprovalComments(Long id) {
        List<ReimbursementApproval> approvals = approvalMapper.selectByReimbursementId(id);
        return Result.success(approvals);
    }

    @Override
    @Transactional
    public Result resubmit(Long id) {
        // 检查报销单是否存在
        Reimbursement reimbursement = reimbursementMapper.selectById(id);
        if (reimbursement == null) {
            return Result.error("报销单不存在");
        }

        // 检查状态是否为已驳回
        if (reimbursement.getStatus() != 2) {
            return Result.error("只能重新提交被驳回的报销单");
        }

        // 更新状态为待审批
        reimbursement.setStatus(0);
        reimbursementMapper.updateById(reimbursement);

        return Result.success();
    }

    @Override
    public Result<?> getDetail(Long id) {
        Reimbursement reimbursement = reimbursementMapper.selectById(id);
        return reimbursement != null ? Result.success(reimbursement) : Result.error("未找到报销记录");
    }
} 