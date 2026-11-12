package com.finance.manage.mapper;

import com.finance.manage.entity.ReimbursementApproval;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ApprovalMapper {
    @Insert("INSERT INTO reimbursement_approval(reimbursement_id, approver_id, status, comment) " +
            "VALUES(#{reimbursementId}, #{approverId}, #{status}, #{comment})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ReimbursementApproval approval);

    @Select("SELECT a.*, u.real_name as approver_name " +
            "FROM reimbursement_approval a " +
            "LEFT JOIN sys_user u ON a.approver_id = u.id " +
            "WHERE a.reimbursement_id = #{reimbursementId} " +
            "ORDER BY a.create_time DESC")
    List<ReimbursementApproval> selectByReimbursementId(Long reimbursementId);
} 