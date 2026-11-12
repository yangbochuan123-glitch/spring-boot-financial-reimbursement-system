package com.finance.manage.mapper;

import com.finance.manage.entity.Reimbursement;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReimbursementMapper {
    @Insert("INSERT INTO reimbursement(applicant_id, type_id, amount, description, status, image_urls) " +
            "VALUES(#{applicantId}, #{typeId}, #{amount}, #{description}, #{status}, #{imageUrls})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Reimbursement reimbursement);

    @Select("<script>" +
            "SELECT r.*, u.real_name as applicant_name, rt.type_name " +
            "FROM reimbursement r " +
            "LEFT JOIN sys_user u ON r.applicant_id = u.id " +
            "LEFT JOIN reimbursement_type rt ON r.type_id = rt.id " +
            "WHERE 1=1 " +
            "<if test='applicantId != null'> AND r.applicant_id = #{applicantId} </if>" +
            "<if test='status != null'> AND r.status = #{status} </if>" +
            "ORDER BY r.create_time DESC" +
            "</script>")
    List<Reimbursement> selectList(@Param("applicantId") Long applicantId, @Param("status") Integer status);

    @Update("UPDATE reimbursement SET status = #{status}, update_time = NOW() WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    @Update("UPDATE reimbursement SET " +
            "status = #{status}, " +
            "type_id = #{typeId}, " +
            "amount = #{amount}, " +
            "description = #{description}, " +
            "applicant_id = #{applicantId}, " +
            "update_time = CURRENT_TIMESTAMP " +
            "WHERE id = #{id}")
    int updateById(Reimbursement reimbursement);

    @Select("SELECT r.*, u.real_name as applicant_name, rt.type_name " +
            "FROM reimbursement r " +
            "LEFT JOIN sys_user u ON r.applicant_id = u.id " +
            "LEFT JOIN reimbursement_type rt ON r.type_id = rt.id " +
            "WHERE r.id = #{id}")
    Reimbursement selectById(Long id);

    /**
     * 根据 status group by 就是分组
     * 1 x
     * 1 x2
     * 2 c
     * 1 -> 2
     * 2 -> 1
     * @return
     */
    @Select("SELECT status, COUNT(*) as count, COALESCE(SUM(amount), 0) as amount " +
            "FROM reimbursement " +
            "GROUP BY status")
    List<Map<String, Object>> selectStatsByStatus();

    /**
     * count() 是mysql的一个统计个数的函数
     * @return
     */
    @Select("SELECT rt.type_name as typeName, COUNT(*) as count, COALESCE(SUM(r.amount), 0) as amount " +
            "FROM reimbursement r " +
            "LEFT JOIN reimbursement_type rt ON r.type_id = rt.id " +
            "GROUP BY r.type_id, rt.type_name")
    List<Map<String, Object>> selectStatsByType();
} 