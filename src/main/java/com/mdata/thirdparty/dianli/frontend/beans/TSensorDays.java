package com.mdata.thirdparty.dianli.frontend.beans;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by administrator on 16/6/26.
 */
public class TSensorDays implements Serializable {
    private String sid;
    private int idx;
    private String days;
    private double smax;
    private double smin;
    private double savg;
    private double slast;
    private Timestamp tmax;
    private Timestamp tmin;
    private Timestamp tlast;
    private long scount;
    private long schange;

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

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public double getSmax() {
        return smax;
    }

    public void setSmax(double smax) {
        this.smax = smax;
    }

    public double getSmin() {
        return smin;
    }

    public void setSmin(double smin) {
        this.smin = smin;
    }

    public double getSavg() {
        return savg;
    }

    public void setSavg(double savg) {
        this.savg = savg;
    }

    public double getSlast() {
        return slast;
    }

    public void setSlast(double slast) {
        this.slast = slast;
    }

    public Timestamp getTmax() {
        return tmax;
    }

    public void setTmax(Timestamp tmax) {
        this.tmax = tmax;
    }

    public Timestamp getTmin() {
        return tmin;
    }

    public void setTmin(Timestamp tmin) {
        this.tmin = tmin;
    }

    public Timestamp getTlast() {
        return tlast;
    }

    public void setTlast(Timestamp tlast) {
        this.tlast = tlast;
    }

    public long getScount() {
        return scount;
    }

    public void setScount(long scount) {
        this.scount = scount;
    }

    public long getSchange() {
        return schange;
    }

    public void setSchange(long schange) {
        this.schange = schange;
    }

    @Override
    public String toString() {
        return "TSensorDays{" +
                "sid='" + sid + '\'' +
                ", idx=" + idx +
                ", days='" + days + '\'' +
                ", smax=" + smax +
                ", smin=" + smin +
                ", savg=" + savg +
                ", slast=" + slast +
                ", tmax=" + tmax +
                ", tmin=" + tmin +
                ", tlast=" + tlast +
                ", scount=" + scount +
                ", schange=" + schange +
                '}';
    }
}
