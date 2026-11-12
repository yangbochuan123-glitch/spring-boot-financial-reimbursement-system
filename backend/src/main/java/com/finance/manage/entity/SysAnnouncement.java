package com.finance.manage.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SysAnnouncement {
    private Long id;
    private String title;
    private String content;
    private Long publisherId;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // Additional fields for frontend display
    private String publisherName;
} 