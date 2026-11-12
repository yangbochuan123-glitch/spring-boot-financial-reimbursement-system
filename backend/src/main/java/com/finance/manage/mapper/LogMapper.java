package com.finance.manage.mapper;

import com.finance.manage.entity.SysLog;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LogMapper {
    @Insert("INSERT INTO sys_log(user_id, operation, method, params, ip) " +
            "VALUES(#{userId}, #{operation}, #{method}, #{params}, #{ip})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(SysLog log);

    /**
     * mybatis 标签来进行逻辑判断
     * if '' 满足if条件 才会进行魔偶胡查询
     * @param username
     * @param operation
     * @return
     */
    @Select("<script>" +
            "SELECT l.*, u.username " +
            "FROM sys_log l " +
            "LEFT JOIN sys_user u ON l.user_id = u.id " +
            "WHERE 1=1 " +
            "<if test='username != null and username != \"\"'> AND u.username LIKE CONCAT('%', #{username}, '%') </if>" +
            "<if test='operation != null and operation != \"\"'> AND l.operation LIKE CONCAT('%', #{operation}, '%') </if>" +
            "ORDER BY l.create_time DESC" +
            "</script>")
    List<SysLog> selectList(@Param("username") String username, @Param("operation") String operation);

    @Delete("DELETE FROM sys_log WHERE id = #{id}")
    int delete(Long id);

    @Delete("DELETE FROM sys_log")
    int clear();
} 