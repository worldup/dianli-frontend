package com.mdata.thirdparty.dianli.frontend.web.controller.api;

import java.io.Serializable;

/**
 * Created by administrator on 16/6/25.
 */
public class Result implements Serializable{
    private int status;
    private String errMsg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

}
