package com.zzlcxt.service;

import com.zzlcxt.modle.Image;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.naming.Name;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 张子龙
 * @since 2023-08-14
 */
public interface ImageService extends IService<Image> {
    /**
     * 功能描述 将图片保存到指定位置 成功返回true 失败返回false
     *
     * @param file,name
     * @return boolean
     * @author
     * @date 2023/8/18
     */
    boolean saveImage(MultipartFile file, String name) throws IOException;


    /**
     * 功能描述 查找当前用户名下的所有图片的filenewname
     *
     * @param name
     * @return com.zzlcxt.modle.Image
     * @author
     * @date 2023/9/20
     */
    List<Object> selectImageUrlByName(String name);

    /**
     * 功能描述 通过文件名删除对应的图片 ,返回值删除的数量，等于0表示没有数据，等于-1表示出现错误
     * @author
     * @date 2023/10/2
     * @param filenewname
     * @return int
     */
    int deleteImageUrlByFilenewname(String filenewname,String name);
}
