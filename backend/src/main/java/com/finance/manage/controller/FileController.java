package com.finance.manage.controller;

import com.finance.manage.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/file")
public class FileController {

    @Value("${file.upload.path}")
    private String uploadPath;

    @Value("${file.upload.url}")
    private String uploadUrl;

    @PostMapping("/upload")
    public Result<?> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("上传文件不能为空");
        }

        // 生成文件存储的相对路径
        String datePath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        String relativePath = datePath + "/" + UUID.randomUUID().toString().replace("-", "") + 
                file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

        // 创建文件存储目录
        File uploadDir = new File(uploadPath + "/" + datePath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 保存文件
        try {
            File dest = new File(uploadPath + "/" + relativePath);
            file.transferTo(dest);
            //返回的是文件的访问路径
            return Result.success(uploadUrl + "/" + relativePath);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("文件上传失败");
        }
    }
} 