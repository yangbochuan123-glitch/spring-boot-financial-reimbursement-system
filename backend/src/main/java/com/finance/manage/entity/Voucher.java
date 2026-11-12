package com.finance.manage.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Voucher {
    private Long id;
    private String voucherNo;
    private Integer type;  // 1-收入，2-支出
    private BigDecimal amount;
    private String description;
    private Long operatorId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // Additional fields for frontend display
    private String operatorName;
    private String typeText;
} 