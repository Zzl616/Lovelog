package com.zzlcxt.service;

import com.zzlcxt.api.Result;
import com.zzlcxt.modle.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.security.NoSuchAlgorithmException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 张子龙
 * @since 2023-08-07
 */
public interface UserService extends IService<User> {

    /**
     * 功能描述 新用户注册
     * @author
     * @date 2023/8/7
     * @param user
     * @return com.zzlcxt.modle.User
     */
    User registered(User user) throws NoSuchAlgorithmException;
    /**
     * 功能描述 通过用户名字查询是否有同名用户,true代表有，false代表没有
     * @author
     * @date 2023/8/9
     * @param name
     * @return boolean
     */
    boolean selectUser(String name);
    /**
     * 功能描述 通过name生成登录的token
     * @author
     * @date 2023/8/9
     * @param name

     * @return java.lang.String
     */
    String login(String name) throws NoSuchAlgorithmException;
    /**
     * 功能描述 验证密码是否正确
     * @author
     * @date 2023/8/9
     * @param name
     * @return boolean
     */
    boolean verifyPassword(String name,String password) throws NoSuchAlgorithmException;
}
