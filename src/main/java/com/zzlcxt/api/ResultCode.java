package com.zzlcxt.api;

/**
 * @version: java version 1.8
 * @Author: Mr Orange
 * @description: 响应结果枚举类
 * @date: 2023-08-08 19:56
 */
public enum  ResultCode{
    /**
     * 功能描述 操作成功
     * @author
     * @date 2023/8/8
     * @param null
     * @return
     */
    SUCCESS("200","操作成功"),
    /**
     * 功能描述 参数检验失败
     * @author
     * @date 2023/8/8
     * @param null
     * @return
     */
    FAILED("500","操作失败"),
    /**
     * 功能描述 参数校验失败
     * @author
     * @date 2023/8/8
     * @param null
     * @return
     */
    VALIDATE_FAILED("404","参数校验失败"),
    /**
     * 功能描述 暂未登录
     * @author
     * @date 2023/8/8
     * @param null
     * @return
     */UNAUTHORIZED("401","暂未登录");
    private String code;
    private String message;
    public String getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
    private ResultCode(String code,String message) {
        this.code = code;
        this.message = message;
    }
}
