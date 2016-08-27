package com.mdata.thirdparty.dianli.frontend.beans;

/**
 * Created by administrator on 16/8/27.
 */
public class TenantLayout {
    private int id;
    private String title;
    private String strongHeader;
    private String smallHeader;
    private String foot;
    private String name;
    private String desc;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStrongHeader() {
        return strongHeader;
    }

    public void setStrongHeader(String strongHeader) {
        this.strongHeader = strongHeader;
    }

    public String getSmallHeader() {
        return smallHeader;
    }

    public void setSmallHeader(String smallHeader) {
        this.smallHeader = smallHeader;
    }

    public String getFoot() {
        return foot;
    }

    public void setFoot(String foot) {
        this.foot = foot;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
