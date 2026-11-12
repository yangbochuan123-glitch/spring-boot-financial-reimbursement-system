package com.finance.manage.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReimbursementApproval {
    private Long id;
    //报销单id
    private Long reimbursementId;
    //申请id
    private Long approverId;
    //审批的状态
    private Integer status;
    //备注字段
    private String comment;
    //创建时间
    private LocalDateTime createTime;
    
    // Additional fields for frontend display
    private String approverName;
} 