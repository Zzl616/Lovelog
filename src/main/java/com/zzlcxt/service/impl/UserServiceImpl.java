package com.zzlcxt.service.impl;

import com.zzlcxt.modle.User;
import com.zzlcxt.dao.UserMapper;
import com.zzlcxt.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 张子龙
 * @since 2023-05-24
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
