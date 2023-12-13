package com.zzlcxt.modle;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author 张子龙
 * @since 2023-11-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("video")
public class Video implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键，唯一标识
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 视频名称
     */
    private String videoname;

    /**
     * 视频大小（单位是字节）
     */
    private long size;

    /**
     * 720p的路径地址
     */
    private String url720;

    /**
     * 1080p的路径地址
     */
    private String url1080;

    /**
     * 4k的路径地址
     */
    private String url4k;

    /**
     * 14440p的路径地址（蓝光）
     */
    private String url1440;

    /**
     * 视频缩略图的路径
     */
    private String thumbnail;

    /**
     * 视频时长(按秒计算）
     */
    private double duration;

    /**
     * 视频格式
     */
    private String format;

    /**
     * 上传时间
     */
    private LocalDateTime uploadTime;

    /**
     * 描述
     */
    private String description;
    /**
     * 原始视频的宽度
     */
    private Double width;
    /**
     * 原始视频的高度
     */
    private Double height;

    /**
     * 原始视频的存储位置
     */
    private String url;

    /**
     * vtt文件的存储路径
     */
    private String vtturl;

    /**
     * 视频标题
     */
    private String title;
}
