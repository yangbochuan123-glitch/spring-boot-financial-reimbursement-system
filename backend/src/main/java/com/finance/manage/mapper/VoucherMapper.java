package com.finance.manage.mapper;

import com.finance.manage.entity.Voucher;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface VoucherMapper {
    @Select("<script>" +
            "SELECT v.*, u.real_name as operator_name, " +
            "CASE v.type WHEN 1 THEN '收入' WHEN 2 THEN '支出' END as type_text " +
            "FROM voucher v " +
            "LEFT JOIN sys_user u ON v.operator_id = u.id " +
            "WHERE 1=1 " +
            "<if test='type != null'> AND v.type = #{type} </if>" +
            "<if test='voucherNo != null and voucherNo != \"\"'> AND v.voucher_no LIKE CONCAT('%', #{voucherNo}, '%') </if>" +
            "ORDER BY v.create_time DESC" +
            "</script>")
    List<Voucher> selectList(@Param("type") Integer type, @Param("voucherNo") String voucherNo);

    @Insert("INSERT INTO voucher(voucher_no, type, amount, description, operator_id) " +
            "VALUES(#{voucherNo}, #{type}, #{amount}, #{description}, #{operatorId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Voucher voucher);

    @Update("UPDATE voucher SET type = #{type}, amount = #{amount}, " +
            "description = #{description} WHERE id = #{id}")
    int update(Voucher voucher);

    @Delete("DELETE FROM voucher WHERE id = #{id}")
    int delete(Long id);

    /**
     * 报销详情
     * @param id
     * @return
     */
    @Select("SELECT v.*, u.real_name as operator_name, " +
            "CASE v.type WHEN 1 THEN '收入' WHEN 2 THEN '支出' END as type_text " +
            "FROM voucher v " +
            "LEFT JOIN sys_user u ON v.operator_id = u.id " +
            "WHERE v.id = #{id}")
    Voucher selectById(Long id);

    @Select("SELECT COALESCE(SUM(amount), 0) FROM voucher WHERE type = #{type}")
    BigDecimal sumAmountByType(Integer type);

    @Select("SELECT MONTH(create_time) as month, COALESCE(SUM(amount), 0) as amount " +
            "FROM voucher " +
            "WHERE YEAR(create_time) = #{year} AND type = #{type} " +
            "GROUP BY MONTH(create_time)")
    List<Map<String, Object>> selectMonthlyStats(@Param("year") Integer year, @Param("type") Integer type);
} 