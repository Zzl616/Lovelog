package com.zzlcxt.config;


import com.zzlcxt.controller.ImageController;
import com.zzlcxt.util.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @version: java version 1.8
 * @Author: Mr Orange
 * @description: jwt拦截器
 * @date: 2023-08-10 10:24
 */
@Component
public class JwtAuthenticationFilter implements HandlerInterceptor { // 实现 HandlerInterceptor 接口来创建自定义过滤器
    private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private JwtTokenUtil jwttokenutil = new JwtTokenUtil();
    // 创建 JWT 工具类实例


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 检查请求类型是否为OPTIONS
        if (!request.getMethod().equalsIgnoreCase(HttpMethod.OPTIONS.toString())) {
            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

            // 在服务器日志中打印出提取到的 Authorization 头部字段值
            logger.info("授权头部字段：{}", authorizationHeader);

            String token = null;

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
                token = authorizationHeader.substring(7);
            }

            // 在服务器日志中打印出从头部字段值提取的令牌
            logger.info("提取的令牌：{}", token);

            if(jwttokenutil.validate(token) == null) {
                throw new ServletException("令牌无效或不存在");
            }
        }

        return true;
    }


}

