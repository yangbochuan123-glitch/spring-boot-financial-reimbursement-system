package com.finance.manage.controller;

import com.finance.manage.common.Result;
import com.finance.manage.entity.SysLog;
import com.finance.manage.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 */
@RestController
@RequestMapping("/api/log")
public class LogController {

    @Autowired
    private LogService logService;

    @GetMapping("/list")
    public Result<?> getList(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String operation) {
        return logService.getList(username, operation);
    }

    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Long id) {
        return logService.delete(id);
    }

    @DeleteMapping("/clear")
    public Result<?> clear() {
        return logService.clear();
    }
} 