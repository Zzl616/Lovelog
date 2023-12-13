package com.zzlcxt.controller;


import com.zzlcxt.api.Result;
import com.zzlcxt.modle.Video;
import com.zzlcxt.service.VideoService;
import com.zzlcxt.util.JwtTokenUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 张子龙
 * @since 2023-11-29
 */
@RestController
@RequestMapping("/video")
public class VideoController {
    private final Logger logger = LoggerFactory.getLogger(VideoController.class);

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private VideoService videoService;

    @Value("${file.serverpath}")
    private String serverpathDirectory;

    @ApiOperation(value = "保存视频")
    @RequestMapping(value = "/savevideo", method = RequestMethod.POST)
    @ResponseBody
    public Result<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String header) {
        logger.info("接收到视频{}", file.getOriginalFilename());


        try {
            String name = jwtTokenUtil.nameByheader(header);
            logger.info("解析用户{}成功", name);

            Video videoInformation = videoService.getVideoInformation(file, name);
            logger.info("获取视频{}的信息成功", file.getOriginalFilename());

            Video videoResolution = videoService.setDefaultResolution(videoInformation);
            logger.info("设置视频{}的默认分辨率成功", file.getOriginalFilename());

            Video videoSpritesheet = videoService.spritesheetCreator(videoResolution);
            logger.info("创建视频{}的精灵图成功", file.getOriginalFilename());

            Video videoVtt = videoService.createVtt(videoSpritesheet);
            logger.info("创建视频{}的VTT文件成功", file.getOriginalFilename());

            int isSaved = videoService.saveVideo(videoVtt);

            if (isSaved > 0) {
                logger.info("保存视频{}成功", file.getOriginalFilename());
                return Result.success();
            } else {
                logger.warn("保存视频{}失败", file.getOriginalFilename());
                return Result.failed(null, "保存视频失败");
            }
        } catch (Exception e) {
            logger.error("处理视频{}时出错: {}", file.getOriginalFilename(), e.getMessage());
            return Result.failed(null, "处理视频失败: " + e.getMessage());
        }

    }
    @ApiOperation(value = "查找对应用户的视频")
    @RequestMapping(value = "/showvideo", method = RequestMethod.POST)
    @ResponseBody
    public Result<List<Map<String, Object>>> showVideo(@RequestHeader("Authorization") String header) {
        String name = jwtTokenUtil.nameByheader(header);
        List<Map<String, Object>> maps = videoService.selectVideoUrlByName(name);


        // 遍历maps，修改每个map中的url值
        for (Map<String, Object> map : maps) {
            // 获取原始的绝对路径
            String url720 = (String) map.get("url720");
            String url1080 = (String) map.get("url1080");
            String url4k = (String) map.get("url4k");
            String url1440 = (String) map.get("url1440");
            String vtturl = (String) map.get("vtturl");

            // 将绝对路径转换为相对路径
            if (url720 != null && url720.startsWith(serverpathDirectory)) {
                map.put("url720", url720.substring(serverpathDirectory.length()));
            }
            if (url1080 != null && url1080.startsWith(serverpathDirectory)) {
                map.put("url1080", url1080.substring(serverpathDirectory.length()));
            }
            if (url4k != null && url4k.startsWith(serverpathDirectory)) {
                map.put("url4k", url4k.substring(serverpathDirectory.length()));
            }
            if (url1440 != null && url1440.startsWith(serverpathDirectory)) {
                map.put("url1440", url1440.substring(serverpathDirectory.length()));
            }
            if (vtturl != null && vtturl.startsWith(serverpathDirectory)) {
                map.put("vtturl", vtturl.substring(serverpathDirectory.length()));
            }
        }

        return Result.success(maps);
    }

}

