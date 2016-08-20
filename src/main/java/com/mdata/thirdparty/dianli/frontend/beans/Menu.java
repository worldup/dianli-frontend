package com.mdata.thirdparty.dianli.frontend.beans;

import java.util.List;

/**
 * Created by administrator on 16/7/18.
 */
public class Menu {
    private List<Menu> subMenu;
    private Long id;
    private Long pid;
    private String name;
    private String resourceId;
    private String url;
    private int idx;
    private String iconClass;
    private String tenantId;

    public List<Menu> getSubMenu() {
        return subMenu;
    }

    public void setSubMenu(List<Menu> subMenu) {
        this.subMenu = subMenu;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getIconClass() {
        return iconClass;
    }

    public void setIconClass(String iconClass) {
        this.iconClass = iconClass;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "subMenu=" + subMenu +
                ", id=" + id +
                ", pid=" + pid +
                ", name='" + name + '\'' +
                ", resourceId='" + resourceId + '\'' +
                ", url='" + url + '\'' +
                ", idx=" + idx +
                ", iconClass='" + iconClass + '\'' +
                ", tenantId='" + tenantId + '\'' +
                '}';
    }
}
