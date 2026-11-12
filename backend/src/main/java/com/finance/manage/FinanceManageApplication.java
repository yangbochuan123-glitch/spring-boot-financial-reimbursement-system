package com.finance.manage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * springboot 入口类
 */
@SpringBootApplication
/**
 * 去扫描mapper接口
 */
@MapperScan("com.finance.manage.mapper")
public class FinanceManageApplication {
    public static void main(String[] args) {
        SpringApplication.run(FinanceManageApplication.class,args);
    }
} 