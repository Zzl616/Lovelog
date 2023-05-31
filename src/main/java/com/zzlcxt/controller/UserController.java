package com.zzlcxt.controller;


import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 张子龙
 * @since 2023-05-24
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @RequestMapping(value = "/hello")
    public String hello(){
        return "hello";
    }

}

