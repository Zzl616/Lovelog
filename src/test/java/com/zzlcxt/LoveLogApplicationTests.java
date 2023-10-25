package com.zzlcxt;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzlcxt.controller.UserController;
import com.zzlcxt.dao.UserMapper;
import com.zzlcxt.api.Result;
import com.zzlcxt.modle.Image;
import com.zzlcxt.modle.User;
import com.zzlcxt.service.UserService;
import com.zzlcxt.service.impl.ImageServiceImpl;
import jdk.nashorn.internal.parser.Token;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


@SpringBootTest(classes = LoveLogApplication.class)
class LoveLogApplicationTests {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private UserController userController;

    @Autowired
    private ImageServiceImpl imageServiceImpl;

    @Test
    void test1() {
        String name = "啊啊啊啊";
        List<Object> images = imageServiceImpl.selectImageUrlByName(name);
        System.out.println(images);
    }

    @Test
    void TestselectPage() {
        IPage page = new Page(1, 2);
        IPage iPage = userMapper.selectPage(page, null);
        System.out.println("当前页码值" + page.getCurrent());
        System.out.println("每页显示数" + page.getSize());
        System.out.println("一共多少页" + page.getPages());
        System.out.println("一共多少条" + page.getTotal());
        System.out.println("数据" + page.getRecords());
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
     *
     * @param
     * @return void
     * @author 张子龙
     * @date 2023/5/23
     */
    @Test
    void TestGetOne() {
        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(now);
        System.out.println(timestamp);
    }

    /**
     * 功能描述 按照条件查询2
     *
     * @param
     * @return void
     * @author 张子龙
     * @date 2023/5/23
     */
    @Test
    void TestGetTwo() {
        String password = userMapper.passwordByName("aaaaaa");
        System.out.println(password);
    }

    /**
     * 功能描述 按照条件查询3
     *
     * @param
     * @return void
     * @author zzl1
     * @date 2023/5/23
     */
    @Test
    void TestGetThree() {
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<User>();
        lqw.lt(User::getAge, 18);
        List<User> users = userMapper.selectList(lqw);
        System.out.println(users);
    }

    @Test
    void Testgetuser() throws NoSuchAlgorithmException {
        String password = "asadgazdxasg";
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(password.getBytes());
        byte[] newPassword = md5.digest();
        System.out.println(password);
        System.out.println(newPassword);
        System.out.println(Arrays.toString(newPassword));
    }

    @Test
    void Testgetuser2() throws NoSuchAlgorithmException {
        User newuser = new User();
//        newuser.setName("aaaaaa");
        newuser.setAge(8);
        newuser.setPassword("aaaaaaaaaaaaaaaaaaaaa");
        newuser.setSex("男");
        Result<User> registered = userController.registered(newuser);
        System.out.println(registered);

    }


}
