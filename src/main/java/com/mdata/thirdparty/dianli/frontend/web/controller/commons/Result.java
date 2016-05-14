package com.mdata.thirdparty.dianli.frontend.web.controller.commons;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by administrator on 16/5/14.
 */
public class Result implements Serializable {

    //code =0 成功  code=-1 失败
    private int code;
    private Map<String,Object> data;
    private String msg;

    public Result() {
        this(0,"success");
    }

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }
}
