package com.gangukeji.xzqn.utils;


public class ResultUtils {

    public static Result success(Object o) {
        Result result = new Result();
        result.setCode(ResultStatus.SUCCESS.getCode());
        result.setMsg(ResultStatus.SUCCESS.getMsg());
        result.setData(o);
        return result;
    }

    public static Result success() {
        return success(null);
    }

    public static Result success(Integer code, String msg, Object o) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(o);
        return result;
    }

    public static Result error(Integer code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static Result error(ResultStatus resultStatus) {
        return error(resultStatus.getCode(), resultStatus.getMsg());
    }

}
