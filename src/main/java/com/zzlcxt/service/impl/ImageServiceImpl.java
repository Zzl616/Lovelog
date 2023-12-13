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
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
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
            String filenewname = nowStrForFileName +
                    randomNum +
                    suffix;
            image.setFilenewname(filenewname);
            File outputFile = new File(imageDirectory, filenewname);
            //使用流式传输，可以在传输过大文件时保证较小的内存占用
            // 使用try-with-resources语句，可以自动关闭在括号内创建的资源（这里是InputStream和OutputStream）
            try (
                    // 从MultipartFile对象获取输入流，这个输入流连接到上传的文件
                    InputStream in = file.getInputStream();
                    // 创建一个输出流，这个输出流连接到要保存的文件
                    OutputStream out = Files.newOutputStream(outputFile.toPath())
            ) {
                // 创建一个缓冲区，用于在读写文件时暂存数据
                byte[] buffer = new byte[1024];
                int bytesRead;
                // 循环读取输入流的数据，直到没有数据可读（read方法返回-1）
                while ((bytesRead = in.read(buffer)) != -1) {
                    // 将缓冲区中的数据写入到输出流，注意只写入实际读取的字节数（bytesRead）
                    out.write(buffer, 0, bytesRead);
                }
            }

            imageMapper.insert(image);

            logger.info("已成功保存图像: {}, 新文件名: {}, 文件路径: {}", originalFilename, filenewname, outputFile.getPath());
            return true;
        } catch (Exception e) {
            logger.error("保存图像时出错: {}", file.getOriginalFilename(), e);
            throw e;
        }
    }

    @Override
    public List<Object> selectImageUrlByName(String name) {
        logger.info("开始搜索用户名{}对应图片列表", name);
        try {
            QueryWrapper<Image> imageQueryWrapper = new QueryWrapper<>();
            imageQueryWrapper.select("filenewname").eq("name",name);
            List<Object> filenewname = imageMapper.selectObjs(imageQueryWrapper);
            logger.info("成功获取用户名{}对应图片列表", name);
            return filenewname;
        } catch (Exception e){
            logger.error("搜索用户名{}对应图片列表失败: {}", name, e.getMessage(), e);
            throw e;
        }
    }


    @Override
    public int deleteImageUrlByFilenewname(String filenewname,String name) {
        logger.info("开始删除指定文件名{}对应的图片", filenewname);
        int result;
        try {
            HashMap<String, Object> deletemap = new HashMap<>();
            deletemap.put("filenewname", filenewname);
            deletemap.put("name", name);
            result = imageMapper.deleteByMap(deletemap);
            if (result > 0) {
                logger.info("成功删除指定文件名{}对应的图片", filenewname);
            } else {
                logger.warn("未找到指定文件名{}的图片，无法执行删除操作", filenewname);
            }
            return result;
        } catch (Exception e) {
            logger.error("删除指定文件名{}对应的图片失败: {}", filenewname, e.getMessage());
            throw e;
        }

    }



}
