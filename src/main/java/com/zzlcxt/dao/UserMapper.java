package com.zzlcxt.dao;

import com.zzlcxt.modle.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 张子龙
 * @since 2023-05-24
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
