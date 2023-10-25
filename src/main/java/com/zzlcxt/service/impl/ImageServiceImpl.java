package com.zzlcxt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zzlcxt.modle.Image;
import com.zzlcxt.dao.ImageMapper;
import com.zzlcxt.service.ImageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.zzlcxt.util.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 张子龙
 * @since 2023-08-14
 */
@Service
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image> implements ImageService {
    private final Logger logger = LoggerFactory.getLogger(ImageService.class);
    @Autowired
    private ImageMapper imageMapper;
    @Autowired
    private JwtTokenUtil jwttokenutil;
    @Value("${file.image}")
    private String imageDirectory;



    @Override
    public boolean saveImage(MultipartFile file,String name) throws IOException {
        if (file.isEmpty()) {
            logger.debug("传入图像不存在");
            return false;
        }

        logger.info("开始保存图像: {}", file.getOriginalFilename());

        try {
            Image image = new Image();
            String originalFilename = file.getOriginalFilename();
            String contentType = file.getContentType();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            double size = file.getSize();
            LocalDateTime now = LocalDateTime.now();
            Random rand = new Random();
            int randomNum = (char) ('a' + rand.nextInt(26));
            image.setName(name);
            image.setFilename(originalFilename);
            image.setSize(size);
            image.setFormat(contentType);
            image.setCreatetime(now);
            image.setUrl(imageDirectory);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
            String nowStrForFileName = now.format(dtf);
            String filenewname = nowStrForFileName+randomNum+suffix;
            image.setFilenewname(filenewname);
            File outputFile = new File(imageDirectory, filenewname);
            file.transferTo(outputFile);

            imageMapper.insert(image);

            logger.info("已成功保存图像: {}, 新文件名: {}, 文件路径: {}", originalFilename, filenewname, outputFile.getPath());
            return true;
        } catch (Exception e) {
            logger.error("保存图像时出错: {}", file.getOriginalFilename(), e);
            return false;
        }
    }

    @Override
    public List<Object> selectImageUrlByName(String name) {
        logger.info("开始搜索用户名{}对应图片列表", name);
        List<Object> filenewname;
        try {
            QueryWrapper<Image> imageQueryWrapper = new QueryWrapper<>();
            imageQueryWrapper.select("filenewname").eq("name",name);
            filenewname = imageMapper.selectObjs(imageQueryWrapper);
            logger.info("成功获取用户名{}对应图片列表", name);
        } catch (Exception e){
            filenewname = Collections.emptyList();
            logger.error("搜索用户名{}对应图片列表失败: {}", name, e.getMessage());
        }
        return filenewname;
    }


    @Override
    public int deleteImageUrlByFilenewname(String filenewname) {
        logger.info("开始删除指定文件名{}对应的图片", filenewname);
        int result;
        try {
            HashMap<String, Object> deletemap = new HashMap<>();
            deletemap.put("filenewname", filenewname);
            result = imageMapper.deleteByMap(deletemap);
            if (result > 0) {
                logger.info("成功删除指定文件名{}对应的图片", filenewname);
            } else {
                logger.warn("未找到指定文件名{}的图片，无法执行删除操作", filenewname);
            }
        } catch (Exception e) {
            result = -1;
            logger.error("删除指定文件名{}对应的图片失败: {}", filenewname, e.getMessage());
        }
        return result;
    }



}
