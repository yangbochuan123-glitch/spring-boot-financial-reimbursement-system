package com.finance.manage.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 报销单的类型
 */
@Data
public class ReimbursementType {
    private Long id;
    //类型名城
    private String typeName;
    //预算金额限制
    private BigDecimal budgetLimit;
    //描述
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 