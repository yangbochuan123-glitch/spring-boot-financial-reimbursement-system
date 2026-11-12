package com.finance.manage.mapper;

import com.finance.manage.entity.SysAnnouncement;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * mapper层 和数据库进行交互
 * 框架会扫描mapper的接口，进行代码，然后读取sql，然后吧sql提交到mysql里面去执行sql
 */
@Mapper
public interface AnnouncementMapper {
    /**
     * @Select做数据的查询 里面是写sql
     * 查询主表 sys_announcement 的信息，在关联sys_user表的real_name，并且给它取别名为publisher_name
     * sql是与mysql这类数据打交道的变成语言
     * select * from sys_user 从sys_user里面查询所有表的数据
     * @return
     */
    @Select("SELECT a.*, u.real_name as publisher_name " +
            "FROM sys_announcement a " +
            "LEFT JOIN sys_user u ON a.publisher_id = u.id " +
            "ORDER BY a.create_time DESC")
    List<SysAnnouncement> selectList();

    @Select("SELECT a.*, u.real_name as publisher_name " +
            "FROM sys_announcement a " +
            "LEFT JOIN sys_user u ON a.publisher_id = u.id " +
            "WHERE a.status = 1 " +
            "ORDER BY a.create_time DESC")
    List<SysAnnouncement> selectPublishedList();

    @Insert("INSERT INTO sys_announcement(title, content, publisher_id, status) " +
            "VALUES(#{title}, #{content}, #{publisherId}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(SysAnnouncement announcement);

    @Update("UPDATE sys_announcement SET title = #{title}, content = #{content}, " +
            "status = #{status} WHERE id = #{id}")
    int update(SysAnnouncement announcement);

    @Delete("DELETE FROM sys_announcement WHERE id = #{id}")
    int delete(Long id);

    @Select("SELECT a.*, u.real_name as publisher_name " +
            "FROM sys_announcement a " +
            "LEFT JOIN sys_user u ON a.publisher_id = u.id " +
            "WHERE a.id = #{id}")
    SysAnnouncement selectById(Long id);
} 