package com.zzlcxt.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zzlcxt.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZonedDateTime;

/**
 * @version: java version 1.8
 * @Author: Mr Orange
 * @description: Jwt 获取token工具类
 * @date: 2023-08-09 14:21
 */
@Service
public class JwtTokenUtil {
    private final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);
    /**
     * 定义一个静态变量SECRET,表示加密解密的密钥，通常储存在配置文件中。
     */
    private static final String SECRET = "${jwt.secret}";


    /**
     * 创建token.
     * @param name 用户
     * @return token字符串
     * 使用指定的HMAC256算法和密钥生成令牌。
     * 然后将一些声明（如发行人、用户名和有效期等）添加到令牌中，
     * 并最终对其进行签名以生成JWT字符串。
     */
    public String createToken(String name) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        logger.info("正在创建JWT令牌...");
        String token = JWT.create()
                .withIssuer("zzl")
                .withClaim("name", name)
                .withIssuedAt(Instant.from(ZonedDateTime.now()))
                .withExpiresAt(Instant.from(ZonedDateTime.now().plusDays(1)))
                .sign(algorithm);
        logger.info("JWT令牌已成功创建。");
        return token;
    }

    /**
     * 验证token.
     * @param token 要进行验证的token
     * @return 验证成功后返回DecodedJWT对象，否则返回null。
     * 核实步骤包括检查签名的有效性，利用相同的算法和秘密秘钥即可完成核实。
     */
    public DecodedJWT validate(String token){
        try{
            logger.info("正在验证JWT令牌...");
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
            logger.info("JWT令牌验证成功。");
            return jwt;
        }catch(Exception e){
            logger.error("JWT令牌验证失败: " + e.getMessage());
            return null;
        }
    }
    /**
     * 功能描述 通过header中的token获取登录用户的name
     * @author
     * @date 2023/9/20
     * @param header
     * @return java.lang.String
     */
    public String nameByheader(String header){
        String token=null;
        if (header != null && header.startsWith("Bearer")) {
            token = header.substring(7);
        }
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
        //验证token是否正确
        String name = jwt.getClaim("name").asString();
        return name;
    }


}

