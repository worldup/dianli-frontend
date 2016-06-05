package com.mdata.thirdparty.dianli.frontend.beans;

/**
 * Created by administrator on 16/6/4.
 */
//对应sqlite的sensor_days这张表
public class SensorDays {
    private String sid;
    private String idx;
    private int days;
    private double smax;
    private double smin;
    private double savg;
    private double slast;
    private long tmax;
    private long tmin;
    private long count;
    private long change;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
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

    public long getTmax() {
        return tmax;
    }

    public void setTmax(long tmax) {
        this.tmax = tmax;
    }

    public long getTmin() {
        return tmin;
    }

    public void setTmin(long tmin) {
        this.tmin = tmin;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getChange() {
        return change;
    }

    public void setChange(long change) {
        this.change = change;
    }

    @Override
    public String toString() {
        return "SensorDays{" +
                "sid='" + sid + '\'' +
                ", idx='" + idx + '\'' +
                ", days=" + days +
                ", smax=" + smax +
                ", smin=" + smin +
                ", savg=" + savg +
                ", slast=" + slast +
                ", tmax=" + tmax +
                ", tmin=" + tmin +
                ", count=" + count +
                ", change=" + change +
                '}';
    }
}
