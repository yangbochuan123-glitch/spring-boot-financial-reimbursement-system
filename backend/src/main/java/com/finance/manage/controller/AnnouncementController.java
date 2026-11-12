package com.finance.manage.controller;

import com.finance.manage.common.Result;
import com.finance.manage.entity.SysAnnouncement;
import com.finance.manage.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * controllershi 控制层
 */
@RestController
@RequestMapping("/api/announcement")
public class AnnouncementController {

    /**
     * 引入AnnouncementService service 面向接口编程的service
     */
    @Autowired
    private AnnouncementService announcementService;

    @GetMapping("/list")
    public Result<?> getList() {
        return announcementService.getList();
    }

    @GetMapping("/published")
    public Result<?> getPublishedList() {
        return announcementService.getPublishedList();
    }

    @PostMapping("/add")
    public Result<?> add(@RequestBody SysAnnouncement announcement) {
        return announcementService.add(announcement);
    }

    @PostMapping("/update")
    public Result<?> update(@RequestBody SysAnnouncement announcement) {
        return announcementService.update(announcement);
    }

    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Long id) {
        return announcementService.delete(id);
    }

    @GetMapping("/detail/{id}")
    public Result<?> getDetail(@PathVariable Long id) {
        return announcementService.getDetail(id);
    }
} 