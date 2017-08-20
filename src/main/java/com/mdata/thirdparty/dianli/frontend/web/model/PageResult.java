package com.mdata.thirdparty.dianli.frontend.web.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by administrator on 17/7/17.
 */

public class PageResult implements Serializable {
    private List result;
    private Integer total;
    private String resultCode;

    public List getResult() {
        return result;
    }

    public void setResult(List result) {
        this.result = result;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public PageResult(List lists){
        this.result=lists;
        this.total=lists==null?0:lists.size();
    }
    public PageResult(List lists,Integer total){
        this.result=lists;
        this.total= total;
    }
}
