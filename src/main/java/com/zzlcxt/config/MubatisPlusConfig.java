package com.zzlcxt.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @version: java version 1.8
 * @Author: zilong
 * @description: mybatis配置
 * @date: 2023-05-23 16:51
 */
@Configuration
public class MubatisPlusConfig {
    /**
     * 功能描述 拦截器配置
     * @author 张子龙
     * @date 2023/5/24
     * @param
     * @return com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor
     */
    @Bean
    public MybatisPlusInterceptor mpInterceptor(){
        //定义拦截器
        MybatisPlusInterceptor mpInterceptor = new MybatisPlusInterceptor();
        //添加分页拦截器
        mpInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        //添加乐观锁拦截器
        mpInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return mpInterceptor;
    }





}
