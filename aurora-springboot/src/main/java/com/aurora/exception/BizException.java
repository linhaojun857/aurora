package com.aurora.exception;


import com.aurora.enums.StatusCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * @author 花未眠
 * 业务异常
 */
@Getter
@AllArgsConstructor
public class BizException extends RuntimeException {

    /**
     * 错误码
     */
    private Integer code = StatusCodeEnum.FAIL.getCode();

    /**
     * 错误信息
     */
    private final String message;

    public BizException(String message) {
        this.message = message;
    }


    public BizException(StatusCodeEnum statusCodeEnum) {
        this.code = statusCodeEnum.getCode();
        this.message = statusCodeEnum.getDesc();
    }


}
