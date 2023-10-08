package com.zzlcxt.dao;

import com.zzlcxt.modle.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.annotation.QueryAnnotation;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 张子龙
 * @since 2023-08-07
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select(value = "select user.password from eating.user where user.name=#{name}")
    String passwordByName(String name);
}
