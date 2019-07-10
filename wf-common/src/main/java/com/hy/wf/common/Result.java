package com.hy.wf.common;

import com.hy.wf.common.exception.ErrorCode;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: hy-wf
 * @description: 返回数据封装
 * @author: jt
 * @create: 2019-01-04 16:26
 **/
@Data
public class Result {
    /** 类型 */
    private Boolean success;

    /** 代码 */
    private String code;

    /** 数据 */
    private Object data;

    public Result() {

    }

    public Result(boolean success) {
        this.success = success;
        this.code = "200";
    }

    public Result(boolean success, String code, Object data) {
        this.success = success;
        this.code = code;
        this.data = data;
    }

    public static Result success() {
        return new Result(true);
    }

    public static Result success(Object data) {
        return new Result(true, "200", data);
    }

    public static Result fail() {
        return new Result(false);
    }

    public static Result fail(ErrorCode errorCode) {
        Map<String, Object> dataMap = new HashMap<String, Object>(1);
        dataMap.put("message", errorCode.getMessage());
        return new Result(false, errorCode.getCode(), dataMap);
    }

    public static Result fail(String code, String data) {
        Map<String, Object> dataMap = new HashMap<String, Object>(1);
        dataMap.put("message", data);
        return new Result(false, code, dataMap);
    }
}
