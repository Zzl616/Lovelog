package com.zzlcxt.controller;


import com.zzlcxt.api.Result;
import com.zzlcxt.service.ImageService;
import com.zzlcxt.util.JwtTokenUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 张子龙
 * @since 2023-08-14
 */
@RestController
@RequestMapping("/image")
public class ImageController {
    private final Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private ImageService imageService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @ApiOperation(value = "保存图片")
    @RequestMapping(value = "/saveimage", method = RequestMethod.POST)
    @ResponseBody
    public Result<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String header) {
        logger.info("接收到图片{}", file.getOriginalFilename());

        String name = jwtTokenUtil.nameByheader(header);
        try {
            // 使用ImageService的saveImage()方法保存文件
            boolean isSaved = imageService.saveImage(file, name);

            if (isSaved) {
                // 如果图片保存成功，则返回成功响应
                logger.info("保存图片{}成功", file.getOriginalFilename());
                return Result.success();
            } else {
                // 如果无法保存图片(例如，因为文件是空的)，则返回错误码
                logger.warn("保存图片{}失败", file.getOriginalFilename());
                return Result.failed(null, "保存图片失败");
            }
        } catch (IOException e) {
            // 文件传输中出现错误时，返回错误响应和消息
            logger.error("文件上传失败, 异常信息: {}", e.getMessage());
            return Result.failed(null, "文件上传失败");
        }
    }

    @ApiOperation(value = "查找对应用户的照片")
    @RequestMapping(value = "/showimage", method = RequestMethod.POST)
    @ResponseBody
    public Result<String> showImage(@RequestHeader("Authorization") String header) {
        String name = jwtTokenUtil.nameByheader(header);
        List<Object> images = imageService.selectImageUrlByName(name);
        return Result.success(images.toString());
    }

    @ApiOperation(value = "删除指定照片")
    @RequestMapping(value = "/deleteImage", method = RequestMethod.POST)
    @ResponseBody
    public Result<String> deleteImage( String filenewname) {
        logger.info("开始删除指定文件名{}的图片", filenewname);
        int i = imageService.deleteImageUrlByFilenewname(filenewname);

        if (i > 0) {
            logger.info("照片删除成功，文件名: {}", filenewname);
            return Result.success(null, "照片删除成功");
        }

        if (i == 0) {
            logger.warn("没有找到对应文件: {}", filenewname);
            return Result.validateFailed(null, "没有找到对应文件");
        }

        if (i == -1) {
            logger.error("照片删除失败，文件名: {}", filenewname);
            return Result.failed(null, "照片删除失败");
        }

        // 当无法识别的情况发生
        logger.error("未知问题导致照片删除失败，文件名: {}", filenewname);
        return Result.failed(null, "照片删除失败");
    }


}

