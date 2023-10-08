package com.zzlcxt.modle;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
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
 * @since 2023-08-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("image")
public class Image implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String url;

    private String name;

    private String filename;
    private String filenewname;

    private Double size;

    private String format;

    private LocalDateTime createtime;

    private LocalDateTime updatetime;

    @Version
    private String version;


}
