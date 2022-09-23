package com.aurora.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 花未眠
 * 接口状态码枚举类
 **/
@Getter
@AllArgsConstructor
public enum StatusCodeEnum {
    /**
     * 成功
     */
    SUCCESS(20000, "操作成功"),
    /**
     * 未登录
     */
    NO_LOGIN(40001, "用户未登录"),
    /**
     * 没有操作权限
     */
    AUTHORIZED(40300, "没有操作权限"),
    /**
     * 系统异常
     */
    SYSTEM_ERROR(50000, "系统异常"),
    /**
     * 失败
     */
    FAIL(51000, "操作失败"),
    /**
     * 参数校验失败
     */
    VALID_ERROR(52000, "参数格式不正确"),
    /**
     * 用户名已存在
     */
    USERNAME_EXIST(52001, "用户名已存在"),
    /**
     * 用户名不存在
     */
    USERNAME_NOT_EXIST(52002, "用户名不存在"),
    /**
     * 文章密码认证未通过
     */
    ARTICLE_ACCESS_FAIL(52003, "文章密码认证未通过"),
    /**
     * qq登录错误
     */
    QQ_LOGIN_ERROR(53001, "qq登录错误");

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 描述
     */
    private final String desc;

}
