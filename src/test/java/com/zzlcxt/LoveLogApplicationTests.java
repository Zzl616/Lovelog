package com.zzlcxt;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzlcxt.dao.UserMapper;
import com.zzlcxt.modle.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class LoveLogApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    void TestGetAll() {
        List<User> users = userMapper.selectList(null);
        System.out.println(users);
    }
    @Test
    void TestselectPage() {
        IPage page = new Page(1,2);
        IPage iPage = userMapper.selectPage(page, null);
        System.out.println("当前页码值"+page.getCurrent());
        System.out.println("每页显示数"+page.getSize());
        System.out.println("一共多少页"+page.getPages());
        System.out.println("一共多少条"+page.getTotal());
        System.out.println("数据"+page.getRecords());
    }
    @Test
    void TestSave() {
        User user = new User();
        user.setName("张子龙");
        user.setAge(12);
        user.setEmail("123@qq.com");
        int insert = userMapper.insert(user);
        System.out.println(insert);
    }

    /**
     * 功能描述 按照条件查询1
     * @author 张子龙
     * @date 2023/5/23
     * @param
     * @return void
     */
    @Test
    void TestGetOne() {
        QueryWrapper qw = new QueryWrapper();
        qw.lt("age",18);
        List<User> users = userMapper.selectList(qw);
        System.out.println(users);
    }
    /**
     * 功能描述 按照条件查询2
     * @author 张子龙
     * @date 2023/5/23
     * @param
     * @return void
     */
    @Test
    void TestGetTwo() {
        QueryWrapper<User> qw = new QueryWrapper<User>();
        qw.lambda().lt(User::getAge,18);
        List<User> users = userMapper.selectList(qw);
        System.out.println(users);
    }
    /**
     * 功能描述 按照条件查询3
     * @author 张子龙
     * @date 2023/5/23
     * @param
     * @return void
     */
    @Test
    void TestGetThree() {
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<User>();
        lqw.lt(User::getAge,18);
        List<User> users = userMapper.selectList(lqw);
        System.out.println(users);
    }
}
