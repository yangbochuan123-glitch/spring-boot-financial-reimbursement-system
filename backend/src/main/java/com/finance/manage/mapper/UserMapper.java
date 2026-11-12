package com.finance.manage.mapper;

import com.finance.manage.entity.SysUser;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 套路
 * 第一步请求先到controller
 * 第二部到serveice
 * 第三个就是 mapper
 */
@Mapper
public interface UserMapper {
    @Select("SELECT * FROM sys_user WHERE username = #{username}")
    SysUser findByUsername(String username);

    @Select("<script>" +
            "SELECT u.*, r.role_name FROM sys_user u " +
            "LEFT JOIN sys_role r ON u.role_id = r.id " +
            "WHERE 1=1 " +
            "<if test='username != null and username != \"\"'> AND u.username LIKE CONCAT('%', #{username}, '%') </if>" +
            "<if test='roleId != null'> AND u.role_id = #{roleId} </if>" +
            "ORDER BY u.create_time DESC" +
            "</script>")
    List<SysUser> selectList(@Param("username") String username, @Param("roleId") Long roleId);

    @Insert("INSERT INTO sys_user(username, password, real_name, role_id, email, phone, status) " +
            "VALUES(#{username}, #{password}, #{realName}, #{roleId}, #{email}, #{phone}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(SysUser user);

    @Update("UPDATE sys_user SET real_name = #{realName}, role_id = #{roleId}, " +
            "email = #{email}, phone = #{phone}, status = #{status} " +
            "WHERE id = #{id}")
    int update(SysUser user);

    @Update("UPDATE sys_user SET password = #{password} WHERE id = #{id}")
    int updatePassword(@Param("id") Long id, @Param("password") String password);

    @Delete("DELETE FROM sys_user WHERE id = #{id}")
    int delete(Long id);

    @Select("SELECT u.*, r.role_name FROM sys_user u " +
            "LEFT JOIN sys_role r ON u.role_id = r.id " +
            "WHERE u.id = #{id}")
    SysUser selectById(Long id);
} 