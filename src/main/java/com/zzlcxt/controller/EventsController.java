package com.zzlcxt.controller;


import com.zzlcxt.api.Result;
import com.zzlcxt.modle.Events;
import com.zzlcxt.service.EventsService;
import com.zzlcxt.util.JwtTokenUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 张子龙
 * @since 2023-10-10
 */
@RestController
@RequestMapping("/events")
public class EventsController {
    private final Logger logger = LoggerFactory.getLogger(EventsController.class);
    @Autowired
    private EventsService eventsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @ApiOperation(value = "上传事件")
    @RequestMapping(value = "/saveevents", method = RequestMethod.POST)
    @ResponseBody
    public Result<Events> uploadEvents(String eventsname, String startTime, String description, @RequestHeader("Authorization") String header) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime newstartTime = LocalDateTime.parse(startTime, formatter);
            String name = jwtTokenUtil.nameByheader(header);
            logger.info("开始上传事件。事件名: {}, 用户名: {}", eventsname, name);

            //检车是否有同名事件
            if (eventsService.selectEventsname(eventsname, name)) {
                logger.warn("有重名事件: {}", eventsname);
                return Result.validateFailed(null, "有重名事件");
            }

            boolean saveEvents = eventsService.saveEvents(eventsname, description, name, newstartTime);
            if (saveEvents) {
                logger.info("事件保存成功: {}", eventsname);
                return Result.success(null, "保存成功");
            } else {
                logger.info("事件保存失败: {}", eventsname);
                return Result.failed(null, "保存失败");
            }
        } catch (Exception e) {
            logger.error("处理上传事件请求时出错，事件名为：{}" , eventsname, e);
            // 可以返回一个表示运行错误的 response 给调用者
            return Result.failed(null,"处理上传事件请求时出错");
        }
    }
    @ApiOperation(value = "查找对应用户的事件")
    @RequestMapping(value = "/showevents", method = RequestMethod.POST)
    @ResponseBody
    public Result<List<Events>> showEvents(@RequestHeader("Authorization") String header) {
        String name = jwtTokenUtil.nameByheader(header);
        List<Events> events = eventsService.selectEventsByName(name);

        return Result.success(events);
    }
    @ApiOperation(value = "删除对应用户的事件")
    @RequestMapping(value = "/deleteevents", method = RequestMethod.POST)
    @ResponseBody
    public Result<Events> deleteEvents(String eventsname,@RequestHeader("Authorization") String header) {
        logger.info("开始删除事件，事件名：{}", eventsname);
        String name = jwtTokenUtil.nameByheader(header);
        logger.info("从header中解析出用户名：{}", name);
        int i = eventsService.deleteEventsByEventsname(eventsname, name);
        if (i > 0) {
            logger.info("事件删除成功");
            return Result.success(null,"删除成功");
        }
        logger.warn("事件删除失败");
        return Result.failed(null,"删除失败");
    }
}

