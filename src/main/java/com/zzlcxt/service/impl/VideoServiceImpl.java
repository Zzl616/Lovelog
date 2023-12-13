package com.zzlcxt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zzlcxt.modle.Video;
import com.zzlcxt.dao.VideoMapper;
import com.zzlcxt.service.VideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.bytedeco.javacv.*;
import org.bytedeco.javacv.Frame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 张子龙
 * @since 2023-11-29
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {
    private final Logger logger = LoggerFactory.getLogger(VideoService.class);
    @Value("${file.video}")
    private String videoDirectory;
    @Value("${file.spritesheet}")
    private String spritesheetDirectory;

    @Value("${file.vtt}")
    private String vttDirectory;

    @Value("${file.serverpath}")
    private String serverpathDirectory;

    @Autowired
    private VideoMapper videoMapper;


    @Override
    public Video setDefaultResolution(Video video) {
        if (video == null || video.getUrl() == null) {
            throw new IllegalArgumentException("video或video的url不是null");
        }

        String url = video.getUrl();
        String newUrl = null;
        int dotIndex = url.lastIndexOf('.');
        if (dotIndex > 0) {
            newUrl = url.substring(0, dotIndex) + "HD" + url.substring(dotIndex);
        }

        FFmpegFrameGrabber grabber = null;
        FFmpegFrameRecorder recorder = null;

        try {
            logger.info("开始设置视频{}的默认分辨率", url);

            // 创建FFmpegFrameGrabber对象
            grabber = new FFmpegFrameGrabber(url);

            // 开始抓取
            grabber.start();

            // 检查原始分辨率
            int height = grabber.getImageHeight();
            int width = grabber.getImageWidth();

            if (height >= 1440 && width >= 2560) {
                // 计算新的宽度，保持原始的宽高比
                int newWidth = width * 1080 / height;

                // 创建FFmpegFrameRecorder对象，并设置输出文件名和目标分辨率
                recorder = new FFmpegFrameRecorder(newUrl, newWidth, 1080);

                // 设置视频比特率
                recorder.setVideoBitrate(grabber.getVideoBitrate());

                // 设置音频通道数
                recorder.setAudioChannels(grabber.getAudioChannels());
                // 设置音频比特率
                recorder.setAudioBitrate(grabber.getAudioBitrate());
                // 设置音频采样率
                recorder.setSampleRate(grabber.getSampleRate());
                // 设置音频编码器
                recorder.setAudioCodec(grabber.getAudioCodec());


                // 设置帧速率
                recorder.setFrameRate(grabber.getFrameRate());

                // 设置视频编码器
                recorder.setVideoCodec(grabber.getVideoCodec());

                // 开始录制
                recorder.start();

                Frame frame;
                while ((frame = grabber.grabFrame()) != null) {
                    recorder.record(frame);
                }

                // 停止录制
                recorder.stop();
                video.setUrl1080(newUrl);
            }
        } catch (Exception e) {
            logger.error("设置视频{}的默认分辨率时出错: {}", url, e.getMessage());
        } finally {
            // 停止抓取
            if (grabber != null) {
                try {
                    grabber.stop();
                } catch (Exception e) {
                    logger.warn("停止抓取视频{}时出错: {}", url, e.getMessage());
                }
            }
            if (recorder != null) {
                try {
                    recorder.stop();
                } catch (Exception e) {
                    logger.warn("停止录制视频{}时出错: {}", newUrl, e.getMessage());
                }
            }
        }

        return video;
    }


    @Override
    public int saveVideo(Video video) {
        logger.info("开始保存视频{}", video.getUrl());
        int insert = videoMapper.insert(video);
        if (insert > 0) {
            logger.info("保存视频{}成功", video.getUrl());
        } else {
            logger.warn("保存视频{}失败", video.getUrl());
        }
        return insert;
    }


    @Override
    public Video createVtt(Video video) {
        logger.info("开始创建视频{}的VTT文件", video.getVideoname());

        try {
            String videoName = video.getVideoname();
            String baseName = videoName.substring(0, videoName.lastIndexOf('.'));
            String vttName = baseName + ".vtt";
            File file = new File(vttDirectory, vttName);
            String vttUrl = file.getAbsolutePath();
            video.setVtturl(vttUrl);
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // 写入WEBVTT头部
            bufferedWriter.write("WEBVTT");
            bufferedWriter.newLine();

            // 视频时长，单位为秒
            double videoDuration = video.getDuration();
            // 抓取间隔，单位为秒
            int grabInterval = 5;
            // 总帧数
            double totalFrames = videoDuration / grabInterval;

            // 假设你有一个spritesheet，每帧的尺寸是
            Double videoWidth = video.getWidth();
            Double videoHeight = video.getHeight();
            int height = 180;
            int width = (int) (videoWidth * height / videoHeight);

            // spritesheet的相对URL
            String absolutePath = video.getThumbnail();
            // 你的服务器根目录
            String serverRoot = serverpathDirectory;
            String url;

            if (absolutePath.startsWith(serverRoot)) {
                url = "../../" +absolutePath.substring(serverRoot.length()).replace('\\', '/');
            } else {
                // 如果绝对路径不以服务器根目录开始，抛出异常或返回错误
                throw new IllegalArgumentException("Invalid thumbnail path: " + absolutePath);
            }

            for (int i = 0; i < totalFrames; i++) {
                int x = i * width;

                // 计算时间戳
                int startSeconds = i * grabInterval;
                int endSeconds = (i + 1) * grabInterval;

                String start = String.format("%02d:%02d:%02d.000", startSeconds / 3600, (startSeconds % 3600) / 60, startSeconds % 60);
                String end = String.format("%02d:%02d:%02d.000", endSeconds / 3600, (endSeconds % 3600) / 60, endSeconds % 60);

                // 写入时间戳和缩略图URL
                bufferedWriter.write(String.valueOf(i + 1));
                bufferedWriter.newLine();
                bufferedWriter.write(start + " --> " + end);
                bufferedWriter.newLine();
                bufferedWriter.write(url + "#xywh=" + x + ",0," + width + "," + height);
                bufferedWriter.newLine();
                bufferedWriter.newLine();
            }

            bufferedWriter.close();
            logger.info("创建视频{}的VTT文件成功", video.getVideoname());
        } catch (IOException ex) {
            logger.error("写入视频{}的VTT文件时出错: {}", video.getVideoname(), ex.getMessage());
        }

        return video;
    }

    @Override
    public List<Map<String, Object>> selectVideoUrlByName(String name) {
        logger.info("开始搜索用户名{}对应视频列表", name);
        try {
            QueryWrapper<com.zzlcxt.modle.Video> videoQueryWrapper = new QueryWrapper<>();
            // 在select()方法中添加"videoname","url720","url1080","url4k"
            videoQueryWrapper.select("videoname","url720","url1080","url4k","url1440","vtturl","format").eq("name",name);
            List<Map<String, Object>> video = videoMapper.selectMaps(videoQueryWrapper);
            logger.info("成功获取用户名{}对应视频列表", name);
            return video;
        } catch (Exception e){
            logger.error("搜索用户名{}对应视频列表失败: {}", name, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public int deleteVideoUrlByVideoname(String videoname, String name) {
        logger.info("开始删除用户名为{}，视频名为{}的视频", name, videoname);

        HashMap<String, Object> deletemap = new HashMap<>();
        deletemap.put("videoname", videoname);
        deletemap.put("name", name);
        int result = videoMapper.deleteByMap(deletemap);

        if (result == 0) {
            logger.warn("没有找到匹配的记录，用户名：{}，视频名：{}", name, videoname);
        } else {
            logger.info("成功删除了{}条记录，用户名：{}，视频名：{}", result, name, videoname);
        }

        return result;
    }



    @Override
    public Video spritesheetCreator(Video video) throws IOException {
        logger.info("开始创建视频{}的精灵图", video.getVideoname());

        String inputVideo = video.getUrl();
        Double videoWidth = video.getWidth();
        Double videoHeight = video.getHeight();
        int spriteHeight = 180;
        int spriteWidth = (int) (videoWidth * spriteHeight / videoHeight);
        long grabInterval = 5 * 1000000;

        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputVideo);
        grabber.start();

        int totalFrames;
        if (grabber.getLengthInTime() <= grabInterval) {
            totalFrames = 1;
        } else {
            totalFrames = (int) (grabber.getLengthInTime() / grabInterval);
        }

        BufferedImage spritesheet = new BufferedImage(spriteWidth * totalFrames, spriteHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = spritesheet.createGraphics();

        Java2DFrameConverter converter = new Java2DFrameConverter();

        for (int i = 0; i < totalFrames; i++) {
            grabber.setTimestamp(i * grabInterval);

            Frame frame = grabber.grabImage();
            BufferedImage bufferedImage = converter.getBufferedImage(frame);

            Image thumbnail = bufferedImage.getScaledInstance(spriteWidth, spriteHeight, Image.SCALE_SMOOTH);

            g.drawImage(thumbnail, i * spriteWidth, 0, null);
        }

        g.dispose();
        grabber.stop();

        String videoName = video.getVideoname();
        String spritesheetName = videoName.substring(0, videoName.lastIndexOf('.'));
        File file = new File(spritesheetDirectory, spritesheetName + ".jpg");
        ImageIO.write(spritesheet, "jpg", file);
        String absolutePath = file.getAbsolutePath();
        video.setThumbnail(absolutePath);

        logger.info("创建视频{}的精灵图成功", video.getVideoname());

        return video;
    }


    /**
     * 功能描述 使用I/O的方式获取分辨率
     */
    @Override
    public Video getVideoInformation(MultipartFile file, String name) throws IOException {
        logger.info("开始获取视频{}的信息", file.getOriginalFilename());

        File newfile;
        FFmpegFrameGrabber grabber = null;
        boolean isGrabberStarted = false;

        try {
            Video video = new Video();
            video.setName(name);
            String originalFilename = file.getOriginalFilename();
            String contentType = file.getContentType();
            video.setFormat(contentType);
            long size = file.getSize();
            video.setSize(size);

            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            LocalDateTime now = LocalDateTime.now();
            video.setUploadTime(now);
            Random rand = new Random();
            int randomNum = (char) ('a' + rand.nextInt(26));
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
            String nowStrForFileName = now.format(dtf);
            String filenewname = nowStrForFileName + randomNum + suffix;
            video.setVideoname(filenewname);

            newfile = new File(videoDirectory, filenewname);
            file.transferTo(newfile);

            grabber = new FFmpegFrameGrabber(newfile);
            grabber.start();
            isGrabberStarted = true;

            double width = grabber.getImageWidth();
            double height = grabber.getImageHeight();
            video.setWidth(width);
            video.setHeight(height);

            long lengthInTime = grabber.getLengthInTime();
            double lengthInSeconds = (double) lengthInTime / 1_000_000;
            video.setDuration(lengthInSeconds);

            String absolutePath = newfile.getAbsolutePath();
            video.setUrl(absolutePath);

            if (height >= 2160) {
                video.setUrl4k(absolutePath);
            } else if (height >= 1440) {
                video.setUrl1440(absolutePath);
            } else if (height >= 1080) {
                video.setUrl1080(absolutePath);
            } else if (height >= 720) {
                video.setUrl720(absolutePath);
            } else {
                video.setUrl720(absolutePath);
            }

            logger.info("获取视频{}的信息成功", file.getOriginalFilename());

            return video;
        } catch (IOException e) {
            logger.error("获取视频{}的信息时出错: {}", file.getOriginalFilename(), e.getMessage());
            throw e;
        } finally {
            if (grabber != null && isGrabberStarted) {
                try {
                    grabber.stop();
                } catch (Exception e) {
                    logger.warn("停止抓取视频{}时出错: {}", file.getOriginalFilename(), e.getMessage());
                }
            }
        }
    }


    //    /**
//     * 功能描述 使用内存的方式获取分辨率
//     */
//    @Override
//    public Video currentResolution(MultipartFile file) throws IOException {
//        FFmpegFrameGrabber grabber = null;
//        try {
//            // 获取文件的字节数组
//            byte[] bytes = file.getBytes();
//            // 使用FFmpegFrameGrabber打开字节数组并获取分辨率
//            grabber = FFmpegFrameGrabber.createDefault(bytes);
//            grabber.start();
//            double width = grabber.getImageWidth();
//            double height = grabber.getImageHeight();
//            Video video = new Video();
//            video.setWidth(width);
//            video.setHeigth(height);
//            return video;
//        } catch (IOException e) {
//            // Log the exception
//            logger.error("Error processing file", e);
//            throw e;
//        } finally {
//            if (grabber != null) {
//                try {
//                    grabber.stop();
//                } catch (Exception e) {
//                    logger.error("Error stopping grabber", e);
//                }
//            }
//        }
//    }
}
