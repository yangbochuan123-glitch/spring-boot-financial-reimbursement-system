package com.finance.manage.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * entity层 表达数据库里面的表的信息
 */
@Data
public class Reimbursement {
    //id字段 java里面叫做属性
    private Long id;
    private Long applicantId;
    private Long typeId;
    private BigDecimal amount;
    //在数据库里面叫做varchar 在Java里面是String
    private String description;
    private Integer status;
    private String imageUrls;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // Additional fields for frontend display
    private String applicantName;
    private String typeName;
} 