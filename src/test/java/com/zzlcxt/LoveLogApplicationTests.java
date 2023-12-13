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
import com.zzlcxt.service.VideoService;
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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(classes = LoveLogApplication.class)
class LoveLogApplicationTests {



    @Autowired
    private VideoService videoService;

    @Test
    void select1() {
        String name = "zhangzilong"; // 这里替换为实际的用户名
        List<Map<String, Object>> result = videoService.selectVideoUrlByName(name);
        System.out.println(result);
        // 检查结果是否为空
        assertNotNull(result, "Result should not be null");

        // 如果你知道预期的结果，你可以添加更多的断言来检查结果
        // 例如，检查结果的大小和内容
        // assertEquals(expectedSize, result.size(), "Result size should be " + expectedSize);
        // assertEquals(expectedContent, result.get(0), "First element should be " + expectedContent);
    }


}
