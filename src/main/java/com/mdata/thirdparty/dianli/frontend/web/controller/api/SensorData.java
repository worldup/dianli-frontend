package com.mdata.thirdparty.dianli.frontend.web.controller.api;

import java.io.Serializable;

/**
 * Created by administrator on 16/6/25.
 */
public class SensorData implements Serializable {
    private String sid;
    private int idx;
    private long tg;
    private double sv;
    private String day;
    private long id;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public long getTg() {
        return tg;
    }

    public void setTg(long tg) {
        this.tg = tg;
    }

    public double getSv() {
        return sv;
    }

    public void setSv(double sv) {
        this.sv = sv;
    }
}
