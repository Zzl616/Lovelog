package com.zzlcxt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zzlcxt.modle.Events;
import com.zzlcxt.dao.EventsMapper;
import com.zzlcxt.service.EventsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzlcxt.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 张子龙
 * @since 2023-10-10
 */
@Service
public class EventsServiceImpl extends ServiceImpl<EventsMapper, Events> implements EventsService {
    private final Logger logger = LoggerFactory.getLogger(EventsServiceImpl.class);
    @Autowired
    private EventsMapper eventsMapper;

    @Override
    public boolean saveEvents(String eventsname, String description, String name, LocalDateTime starttime) {
        try {
            logger.info("开始保存{}的{}事件", name, eventsname);
            Events events = new Events();
            events.setEventsname(eventsname);
            events.setDescription(description);
            events.setName(name);
            events.setStarttime(starttime);
            events.setCreatetime(LocalDateTime.now());
            eventsMapper.insert(events);
            logger.info("保存{}的{}事件成功", name, events);
            return true;
        } catch (Exception ex) {
            logger.error("保存事件失败: ", ex);
            return false;
        }
    }

    @Override
    public List<Events> selectEventsByName(String name) {
        List<Events> eventsList = null;
        try {
            logger.info("开始查找{}的所有事件", name);
            QueryWrapper<Events> eventsQueryWrapper = new QueryWrapper<>();
            eventsQueryWrapper.eq("name", name);
            eventsList = eventsMapper.selectList(eventsQueryWrapper);
            logger.info("查找{}的所有事件成功", name);
        } catch (Exception ex) {
            logger.error("通过用户名查询失败，错误：", ex);
        }
        return eventsList;
    }

    @Override
    public boolean selectEventsname(String eventsname, String name) {
        try {
            logger.info("开始用户{}的{}事件是否有相同事件名", name,eventsname);
            QueryWrapper<Events> eventsQueryWrapper = new QueryWrapper<>();
            eventsQueryWrapper.eq("name", name).eq("eventsname",eventsname);
            Integer integer = eventsMapper.selectCount(eventsQueryWrapper);
            String message = (integer > 0) ? "查询到有同名事件名" : "未查询到有同名事件名";
            logger.info(message);
            return integer > 0;
        } catch (Exception ex) {
            logger.error("查找同名事件过程出现错误：", ex);
            return false;
        }

    }

    @Override
    public int deleteEventsByEventsname(String eventsname, String name) {
        logger.info("开始删除指定事件名{}对应的事件", eventsname);
        int result;
        try {
            HashMap<String, Object> deletemap = new HashMap<>();
            deletemap.put("eventsname", eventsname);
            deletemap.put("name", name);
            result = eventsMapper.deleteByMap(deletemap);
            if (result > 0) {
                logger.info("成功删除指定事件{}", eventsname);
            } else {
                logger.warn("未找到指定事件名{}的事件，无法执行删除操作", eventsname);
            }
        }catch (Exception e) {
            result = -1;
            logger.error("删除指定事件{}失败: {}", eventsname, e.getMessage());
        }

        return result;
    }
}
