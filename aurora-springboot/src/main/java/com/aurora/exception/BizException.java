package com.aurora.exception;


import com.aurora.enums.StatusCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BizException extends RuntimeException {

    private Integer code = StatusCodeEnum.FAIL.getCode();

    private final String message;

    public BizException(String message) {
        this.message = message;
    }

    public BizException(StatusCodeEnum statusCodeEnum) {
        this.code = statusCodeEnum.getCode();
        this.message = statusCodeEnum.getDesc();
    }

}
