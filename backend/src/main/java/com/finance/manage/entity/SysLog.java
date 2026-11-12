package com.finance.manage.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SysLog {
    private Long id;
    private Long userId;
    private String operation;
    private String method;
    private String params;
    private String ip;
    private LocalDateTime createTime;
    
    // Additional fields for frontend display
    private String username;
} 