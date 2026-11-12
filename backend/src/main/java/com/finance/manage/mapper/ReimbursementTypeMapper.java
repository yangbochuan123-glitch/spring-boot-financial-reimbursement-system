package com.finance.manage.mapper;

import com.finance.manage.entity.ReimbursementType;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ReimbursementTypeMapper {
    /**
     * reimbursement_type 报销单类型表
     * @return
     */
    @Select("SELECT * FROM reimbursement_type ORDER BY create_time DESC")
    List<ReimbursementType> selectList();

    @Insert("INSERT INTO reimbursement_type(type_name, budget_limit, description) " +
            "VALUES(#{typeName}, #{budgetLimit}, #{description})")
    //id 一般是自增 1 2 3
    @Options(useGeneratedKeys = true, keyProperty = "id")//会把mysql自动升成的id设置 返回结果int
    int insert(ReimbursementType type);

    @Update("UPDATE reimbursement_type SET type_name = #{typeName}, " +
            "budget_limit = #{budgetLimit}, description = #{description} " +
            "WHERE id = #{id}")
    int update(ReimbursementType type);

    @Delete("DELETE FROM reimbursement_type WHERE id = #{id}")
    int delete(Long id);

    @Select("SELECT * FROM reimbursement_type WHERE id = #{id}")
    ReimbursementType selectById(Long id);
} 