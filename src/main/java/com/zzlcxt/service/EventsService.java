package com.zzlcxt.service;

import com.zzlcxt.modle.Events;
import com.baomidou.mybatisplus.extension.service.IService;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 张子龙
 * @since 2023-10-10
 */
public interface EventsService extends IService<Events> {
    /**
     * 功能描述 保存事件方法，成功返回true，返回false
     * @author
     * @date 2023/10/16
     * @param eventsname
     * @param description
     * @param name
     * @param starttime
     * @return boolean
     */
    boolean saveEvents(String eventsname, String description, String name, LocalDateTime starttime);

    /**
     * 功能描述 通过用户名查找用户名下所有事件v
     * @author
     * @date 2023/10/16
     * @param name
     * @return java.util.List<com.zzlcxt.modle.Events>
     */
    List<Events> selectEventsByName(String name);
    /**
     * 功能描述 查找同一用户是否有相同的事件名
     * @author
     * @date 2023/10/16
     * @param eventsname
     * @param name
     * @return boolean
     */
    boolean selectEventsname(String eventsname,String name);
    /**
     * 功能描述 通过事件名和用户名删除指定的事件
     * @author
     * @date 2023/10/16
     * @param eventsname
     * @param name
     * @return int
     */
    int deleteEventsByEventsname(String eventsname,String name);
}
