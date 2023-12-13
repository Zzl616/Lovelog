package com.zzlcxt.service;

import com.zzlcxt.modle.Video;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 张子龙
 * @since 2023-11-29
 */
public interface VideoService extends IService<Video> {

    /**
     * 功能描述 设置视频的默认分辨率,将默认分辨率视频保存到指定位置，
     *
     * @return boolean
     * @author
     * @date 2023/11/30
     */
    Video setDefaultResolution(Video video) ;
    /**
     * 功能描述 获取当前视频的信息，返回一个video类,
     *
     * @return java.lang.String
     * @author
     * @date 2023/11/30
     */
    Video getVideoInformation (MultipartFile file,String name) throws IOException;

    /**
     * 功能描述 根据视频，每十秒创建缩略图，并放到Spritesheet图中（一个图片集）
     *
     * @param
     * @return void
     * @author
     * @date 2023/12/2
     */
    Video spritesheetCreator(Video video) throws IOException;

    /**
     * 功能描述 将video的内容存储到数据库
     *
     * @param
     * @return void
     * @author
     * @date 2023/12/5
     */
    int saveVideo(Video video);

    /**
     * 功能描述  生成vtt文件
     * @author
     * @date 2023/12/6
     * @param video
     * @return com.zzlcxt.modle.Video
     */
    Video createVtt(Video video) throws IOException;

    /**
     * 功能描述 查找当前用户名下的所有视频
     *
     * @param name
     * @return com.zzlcxt.modle.Image
     * @author
     * @date 2023/9/20
     */
    List<Map<String, Object>> selectVideoUrlByName(String name);

    /**
     * 功能描述 通过Videoname删除Video
     * @author
     * @date 2023/12/7
     * @param Videoname
     * @return int
     */
    int deleteVideoUrlByVideoname(String Videoname,String name);
}
