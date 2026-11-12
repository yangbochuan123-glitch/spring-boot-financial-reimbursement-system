package com.finance.manage.service.impl;

import com.finance.manage.common.Result;
import com.finance.manage.entity.SysAnnouncement;
import com.finance.manage.mapper.AnnouncementMapper;
import com.finance.manage.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Service 是一个service
 */
@Service
public class AnnouncementServiceImpl implements AnnouncementService {

    @Autowired
    private AnnouncementMapper announcementMapper;

    @Override
    public Result<?> getList() {
        List<SysAnnouncement> list = announcementMapper.selectList();
        return Result.success(list);
    }

    @Override
    public Result<?> getPublishedList() {
        List<SysAnnouncement> list = announcementMapper.selectPublishedList();
        return Result.success(list);
    }

    @Override
    public Result<?> add(SysAnnouncement announcement) {
        int rows = announcementMapper.insert(announcement);
        return rows > 0 ? Result.success() : Result.error("添加失败");
    }

    @Override
    public Result<?> update(SysAnnouncement announcement) {
        int rows = announcementMapper.update(announcement);
        return rows > 0 ? Result.success() : Result.error("更新失败");
    }

    @Override
    public Result<?> delete(Long id) {
        int rows = announcementMapper.delete(id);
        return rows > 0 ? Result.success() : Result.error("删除失败");
    }

    @Override
    public Result<?> getDetail(Long id) {
        SysAnnouncement announcement = announcementMapper.selectById(id);
        return announcement != null ? Result.success(announcement) : Result.error("公告不存在");
    }
} 