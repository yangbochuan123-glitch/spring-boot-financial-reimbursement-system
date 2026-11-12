package com.finance.manage.service;

import com.finance.manage.common.Result;
import com.finance.manage.entity.SysAnnouncement;

public interface AnnouncementService {
    Result<?> getList();
    Result<?> getPublishedList();
    Result<?> add(SysAnnouncement announcement);
    Result<?> update(SysAnnouncement announcement);
    Result<?> delete(Long id);
    Result<?> getDetail(Long id);
} 