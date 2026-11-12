-- Schema only: no original user, financial, log, or attachment records.
CREATE DATABASE IF NOT EXISTS finance_manage DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE finance_manage;

CREATE TABLE `reimbursement` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `applicant_id` bigint NOT NULL COMMENT '申请人ID',
  `type_id` bigint NOT NULL COMMENT '报销类型ID',
  `amount` decimal(10,2) NOT NULL COMMENT '报销金额',
  `description` text COLLATE utf8mb4_general_ci COMMENT '报销说明',
  `status` tinyint DEFAULT '0' COMMENT '状态：0-待审核，1-已通过，2-已驳回',
  `image_urls` text COLLATE utf8mb4_general_ci COMMENT '凭证图片URL，多个用逗号分隔',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='报销单表';

CREATE TABLE `reimbursement_approval` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `reimbursement_id` bigint NOT NULL COMMENT '报销单ID',
  `approver_id` bigint NOT NULL COMMENT '审批人ID',
  `status` tinyint NOT NULL COMMENT '审批状态：1-通过，2-驳回',
  `comment` text COLLATE utf8mb4_general_ci COMMENT '审批意见',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `reimbursement_id` (`reimbursement_id`),
  KEY `approver_id` (`approver_id`),
  CONSTRAINT `reimbursement_approval_ibfk_1` FOREIGN KEY (`reimbursement_id`) REFERENCES `reimbursement` (`id`),
  CONSTRAINT `reimbursement_approval_ibfk_2` FOREIGN KEY (`approver_id`) REFERENCES `sys_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='报销审批意见表';

CREATE TABLE `reimbursement_type` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `type_name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '报销类型名称',
  `budget_limit` decimal(10,2) DEFAULT NULL COMMENT '预算限额',
  `description` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '类型描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='报销类型表';

CREATE TABLE `sys_announcement` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(200) COLLATE utf8mb4_general_ci NOT NULL COMMENT '公告标题',
  `content` text COLLATE utf8mb4_general_ci NOT NULL COMMENT '公告内容',
  `publisher_id` bigint NOT NULL COMMENT '发布人ID',
  `status` tinyint DEFAULT '1' COMMENT '状态：0-未发布，1-已发布',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统公告表';

CREATE TABLE `sys_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL COMMENT '用户ID',
  `operation` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作类型',
  `method` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求方法',
  `params` text COLLATE utf8mb4_general_ci COMMENT '请求参数',
  `ip` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'IP地址',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统日志表';

CREATE TABLE `sys_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `role_code` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色编码',
  `description` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角色描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色表';

CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `real_name` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '真实姓名',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `email` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手机号',
  `status` tinyint DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户表';

CREATE TABLE `voucher` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `voucher_no` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '凭证编号',
  `type` tinyint NOT NULL COMMENT '类型：1-收入，2-支出',
  `amount` decimal(10,2) NOT NULL COMMENT '金额',
  `description` text COLLATE utf8mb4_general_ci COMMENT '说明',
  `operator_id` bigint NOT NULL COMMENT '操作人ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_voucher_no` (`voucher_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='凭证表';
