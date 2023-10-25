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
 * @since 2023-10-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("events")
public class Events implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 事件名称
     */
    private String eventsname;

    /**
     * 创建时间
     */
    private LocalDateTime createtime;

    /**
     * 修改时间
     */
    private LocalDateTime updatetime;

    /**
     * 起始时间
     */
    private LocalDateTime starttime;

    /**
     * 时间节点（距离起始时间的天数）
     */
    private String timenode;

    /**
     * 描述
     */
    private String description;


}
