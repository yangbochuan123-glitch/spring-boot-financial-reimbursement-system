package com.finance.manage.mapper;

import com.finance.manage.entity.SysRole;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RoleMapper {
    @Select("SELECT * FROM sys_role ORDER BY id")
    List<SysRole> selectList();

    @Select("SELECT * FROM sys_role WHERE id = #{id}")
    SysRole selectById(Long id);

    @Insert("INSERT INTO sys_role(role_name, role_code, description) " +
            "VALUES(#{roleName}, #{roleCode}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(SysRole role);

    @Update("UPDATE sys_role SET role_name = #{roleName}, " +
            "role_code = #{roleCode}, description = #{description} " +
            "WHERE id = #{id}")
    int update(SysRole role);

    @Delete("DELETE FROM sys_role WHERE id = #{id}")
    int delete(Long id);

    @Select("SELECT COUNT(*) FROM sys_user WHERE role_id = #{roleId}")
    int countUsersByRoleId(Long roleId);
} 