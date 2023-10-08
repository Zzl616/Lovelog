package com.zzlcxt.api;

/**
 * @version: java version 1.8
 * @Author: Mr Orange
 * @description: 响应结果返回
 * @date: 2023-08-07 13:20
 */
public class Result<T> {
    private String code;
    private String msg;
    private T data;


    public Result() {

    }

    public Result(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 功能描述 成功返回结果
     *
     * @return com.zzlcxt.api.Result<T>
     * @author
     * @date 2023/8/8
     */
    public static <T> Result<T> success(T data) {
        return new Result<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 功能描述 成功返回结果 无输出对象
     *
     * @param
     * @return com.zzlcxt.api.Result<T>
     * @author
     * @date 2023/8/18
     */
    public static <T> Result<T> success() {
        return new Result<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage());
    }

    /**
     * 功能描述 成功返回结果,自定义返回信息
     *
     * @param
     * @return com.zzlcxt.api.Result<T>
     * @author
     * @date 2023/8/8
     */
    public static <T> Result<T> success(T data, String msg) {
        return new Result<T>(ResultCode.SUCCESS.getCode(), msg, data);
    }

    /**
     * 功能描述 失败返回结果
     *
     * @param data
     * @return com.zzlcxt.api.Result<T>
     * @author
     * @date 2023/8/8
     */
    public static <T> Result<T> failed(T data) {
        return new Result<T>(ResultCode.FAILED.getCode(), ResultCode.FAILED.getMessage(), data);
    }
    /**
     * 功能描述 失败返回结果 无输出对象
     *
     * @param
     * @return com.zzlcxt.api.Result<T>
     * @author
     * @date 2023/8/18
     */
    public static <T> Result<T> failed() {
        return new Result<T>(ResultCode.FAILED.getCode(), ResultCode.FAILED.getMessage());
    }

    /**
     * 功能描述 失败返回结果,自定义返回信息
     *
     * @param data
     * @param msg
     * @return com.zzlcxt.api.Result<T>
     * @author
     * @date 2023/8/8
     */
    public static <T> Result<T> failed(T data, String msg) {
        return new Result<T>(ResultCode.FAILED.getCode(), msg, data);
    }

    /**
     * 功能描述 参数校验失败返回结果
     *
     * @param data
     * @return com.zzlcxt.api.Result<T>
     * @author
     * @date 2023/8/8
     */
    public static <T> Result<T> validateFailed(T data) {
        return new Result<T>(ResultCode.VALIDATE_FAILED.getCode(), ResultCode.VALIDATE_FAILED.getMessage(), data);
    }
    /**
     * 功能描述 失败返回结果 无输出对象
     *
     * @param
     * @return com.zzlcxt.api.Result<T>
     * @author
     * @date 2023/8/18
     */
    public static <T> Result<T> validateFailed() {
        return new Result<T>(ResultCode.VALIDATE_FAILED.getCode(), ResultCode.VALIDATE_FAILED.getMessage());
    }

    /**
     * 功能描述 参数校验失败返回结果,自定义返回信息
     *
     * @param data
     * @param msg
     * @return com.zzlcxt.api.Result<T>
     * @author
     * @date 2023/8/8
     */
    public static <T> Result<T> validateFailed(T data, String msg) {
        return new Result<T>(ResultCode.VALIDATE_FAILED.getCode(), msg, data);
    }

    /**
     * 功能描述 暂未登录
     *
     * @param data
     * @return com.zzlcxt.api.Result<T>
     * @author
     * @date 2023/8/8
     */
    public static <T> Result<T> unauthorized(T data) {
        return new Result<T>(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMessage(), data);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        sb.append("\"code\":\"").append(this.code).append('"');
        sb.append(",\"msg\":\"").append(this.msg).append('"');
        sb.append(",\"data\":").append(this.data);
        sb.append('}');
        return sb.toString();
    }
}