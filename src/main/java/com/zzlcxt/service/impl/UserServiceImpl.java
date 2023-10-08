package com.zzlcxt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zzlcxt.api.Result;
import com.zzlcxt.modle.User;
import com.zzlcxt.dao.UserMapper;
import com.zzlcxt.service.ImageService;
import com.zzlcxt.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzlcxt.util.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 张子龙
 * @since 2023-08-07
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private final Logger logger = LoggerFactory.getLogger(ImageService.class);
    @Autowired
    private UserMapper userMapper;
    private JwtTokenUtil jwttokenutil = new JwtTokenUtil();

    @Override
    public User registered(User user) {
        try {
            logger.info("开始注册用户: {}", user.getName());
            String password = user.getPassword();
            //将密码进行md5加密
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(password.getBytes());
            byte[] newPassword = md5.digest();
            user.setPassword(Arrays.toString(newPassword));
            user.setCreatetime(Timestamp.valueOf(LocalDateTime.now()));
            //将信息写入数据库
            userMapper.insert(user);
            logger.info("用户 {} 已成功注册", user.getName());
        } catch (NoSuchAlgorithmException e) {
            logger.error("F加密密码失败: ", e);
        } catch (Exception ex) {
            logger.error("注册用户失败: ", ex);
        }
        return user;
    }


    @Override
    public boolean selectUser(String name) {
        try {
            logger.info("开始查找是否有同名用户{}", name);

            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("name", name);
            Integer users = userMapper.selectCount(userQueryWrapper);

            String message = (users > 0) ? "查询到有同名用户" : "未查询到同名用户";
            logger.info(message);

            return users > 0;
        }catch (Exception e) {
            logger.error("查找同名用户过程出现错误", e);
            throw e;
        }
    }


    @Override
    public String login(String name) {
        return jwttokenutil.createToken(name);
    }

    @Override
    public boolean verifyPassword(String name, String password) throws NoSuchAlgorithmException {
        try {
            logger.info("开始验证用户名{} 的密码。",name);
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(password.getBytes());
            byte[] newPassword = md5.digest();
            String oldPassword = userMapper.passwordByName(name);
            boolean result = Objects.equals(Arrays.toString(newPassword), oldPassword);

            if(result){
                logger.info("用户名{} 密码验证成功。",name);
            } else {
                logger.info("用户名{} 密码验证失败。",name);
            }

            return result;
        } catch (NoSuchAlgorithmException e) {
            logger.error("MD5加密过程出错", e);
            throw e;
        } catch(Exception e){
            logger.error("验证密码过程出现错误", e);
            throw e;
        }
    }


}
