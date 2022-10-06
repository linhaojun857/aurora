package com.aurora.model.vo;

import com.aurora.enums.StatusCodeEnum;
import lombok.Data;
import static com.aurora.enums.StatusCodeEnum.*;

@Data
public class ResultVO<T> {

    private Boolean flag;

    private Integer code;

    private String message;

    private T data;

    public static <T> ResultVO<T> ok() {
        return restResult(true, null, SUCCESS.getCode(), SUCCESS.getDesc());
    }

    public static <T> ResultVO<T> ok(T data) {
        return restResult(true, data, SUCCESS.getCode(), SUCCESS.getDesc());
    }

    public static <T> ResultVO<T> ok(T data, String message) {
        return restResult(true, data, SUCCESS.getCode(), message);
    }

    public static <T> ResultVO<T> fail() {
        return restResult(false, null, FAIL.getCode(), FAIL.getDesc());
    }

    public static <T> ResultVO<T> fail(StatusCodeEnum statusCodeEnum) {
        return restResult(false, null, statusCodeEnum.getCode(), statusCodeEnum.getDesc());
    }

    public static <T> ResultVO<T> fail(String message) {
        return restResult(false, message);
    }

    public static <T> ResultVO<T> fail(T data) {
        return restResult(false, data, FAIL.getCode(), FAIL.getDesc());
    }

    public static <T> ResultVO<T> fail(T data, String message) {
        return restResult(false, data, FAIL.getCode(), message);
    }

    public static <T> ResultVO<T> fail(Integer code, String message) {
        return restResult(false, null, code, message);
    }

    private static <T> ResultVO<T> restResult(Boolean flag, String message) {
        ResultVO<T> apiResultVO = new ResultVO<>();
        apiResultVO.setFlag(flag);
        apiResultVO.setCode(flag ? SUCCESS.getCode() : FAIL.getCode());
        apiResultVO.setMessage(message);
        return apiResultVO;
    }

    private static <T> ResultVO<T> restResult(Boolean flag, T data, Integer code, String message) {
        ResultVO<T> apiResultVO = new ResultVO<>();
        apiResultVO.setFlag(flag);
        apiResultVO.setData(data);
        apiResultVO.setCode(code);
        apiResultVO.setMessage(message);
        return apiResultVO;
    }

}
