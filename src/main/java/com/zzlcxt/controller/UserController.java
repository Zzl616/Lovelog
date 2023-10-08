package com.zzlcxt.controller;


import com.baomidou.mybatisplus.core.assist.ISqlRunner;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zzlcxt.api.Result;
import com.zzlcxt.modle.User;
import com.zzlcxt.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 张子龙
 * @since 2023-08-07
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private HttpServletResponse response;

    @ApiOperation(value = "用户注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public Result<User> registered(User user) throws NoSuchAlgorithmException {
        logger.info("接收到注册用户{}", user.getName());

        String name = user.getName();
        String sex = user.getSex();
        String password = user.getPassword();
        if (name == null || name.length() < 4 || name.length() > 20) {
            logger.debug("用户名长度应大于等于4位小于等于20位");
            return Result.validateFailed(user, "用户名长度应大于等于4位");
        }
        if (password == null || password.length() < 6 || password.length() > 20) {
            logger.debug("密码长度应为大于等于6位");
            return Result.validateFailed(user, "密码长度应为大于等于6位小于等于20");
        }
        //获取同名用户查看是否重名
        boolean userexist = userService.selectUser(name);
        if (userexist) {
            logger.debug("用户名已存在");
            return Result.validateFailed(user, "用户名已存在");
        }
        if (!"男".equals(sex) && !"女".equals(sex)) {
            logger.debug("性别有误");
            return Result.validateFailed(user, "性别有误");
        }
        //将信息写入数据库
        userService.registered(user);
        logger.info("用户 {} 注册成功", user.getName());
        return Result.success(user);
    }

    @ApiOperation(value = "用户登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Result<User> login(String name, String password) throws NoSuchAlgorithmException {
        logger.info("接收到用户{}登录信息", name);
        boolean verifyPassword = userService.verifyPassword(name, password);
        //比较密码是否匹配

        if (verifyPassword) {
            String token = userService.login(name);
            //执行登陆函数并获取token
            response.addHeader("Authorization", "Bearer " + token);
            //在http响应头中添加token
            logger.info("用户{}登录成功", name);
            return Result.success(null);
        }

        logger.error("用户{}登录失败,用户名或密码错误", name);
        return Result.validateFailed(null, "用户名或密码有误");
    }

}

