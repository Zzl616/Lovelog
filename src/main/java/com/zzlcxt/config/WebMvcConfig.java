package com.zzlcxt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 14502
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Value("${url.Front-end}")
    private String frontEnd;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 配置跨域的路径和规则
        registry.addMapping("/**")
                .allowedOrigins(frontEnd)
                // 这应该是你的前端应用地址
                .allowedMethods("GET", "POST", "OPTIONS", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true)
                .allowedHeaders("*")
                .exposedHeaders("Authorization");
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthenticationFilter)
                // 注册jwtAuthenticationFilter拦截器
                .addPathPatterns("/**")
                // 设置拦截路径为所有路径
                .excludePathPatterns("/user/**","/doc.html","/swagger-ui.html","/webjars/**","/v2/api-docs",
                        "/swagger-resources");
                // 排除"/user/下所有
    }

}
