package com.mdata.thirdparty.dianli.frontend.web.model;

import javax.persistence.*;

@Entity
@Table(name = "t_bileiqi_sensors_mapping")
public class BileiqiSensorMapping {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "pole")
    private String pole;
    @Column(name = "type")

    private String type;
    @Column(name = "bl_wd_sid")

    private String blWdSid;
    @Column(name = "bl_lj_sid")

    private String blLjSid;
    @Column(name = "bl_dl_sid")

    private String blDlSid;
    @Column(name = "tq_wd_sid")

    private String tqWdSid;
    @Column(name = "tq_sd_sid")

    private String tqSdSid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPole() {
        return pole;
    }

    public void setPole(String pole) {
        this.pole = pole;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBlWdSid() {
        return blWdSid;
    }

    public void setBlWdSid(String blWdSid) {
        this.blWdSid = blWdSid;
    }

    public String getBlLjSid() {
        return blLjSid;
    }

    public void setBlLjSid(String blLjSid) {
        this.blLjSid = blLjSid;
    }

    public String getBlDlSid() {
        return blDlSid;
    }

    public void setBlDlSid(String blDlSid) {
        this.blDlSid = blDlSid;
    }

    public String getTqWdSid() {
        return tqWdSid;
    }

    public void setTqWdSid(String tqWdSid) {
        this.tqWdSid = tqWdSid;
    }

    public String getTqSdSid() {
        return tqSdSid;
    }

    public void setTqSdSid(String tqSdSid) {
        this.tqSdSid = tqSdSid;
    }
}
