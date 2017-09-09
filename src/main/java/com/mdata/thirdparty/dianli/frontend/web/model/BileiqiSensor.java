package com.mdata.thirdparty.dianli.frontend.web.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by administrator on 17/8/20.
 */
@Entity
@Table(name="t_bileiqi_sensors")
public class BileiqiSensor implements Serializable {
    @Id
    @Column(name = "sid")
    private String sid;
    @Column(name="sid_addr")
    private String sidAddr;
    @Column(name="type")
    private String type;
    @Column(name="unit")
    private String unit;
    @Column(name="station")
    private String station;
    @Column(name="pole")
    private String pole;

    public String getPole() {
        return pole;
    }

    public void setPole(String pole) {
        this.pole = pole;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSidAddr() {
        return sidAddr;
    }

    public void setSidAddr(String sidAddr) {
        this.sidAddr = sidAddr;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }
}
