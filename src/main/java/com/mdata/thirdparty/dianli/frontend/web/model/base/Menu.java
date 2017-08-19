package com.mdata.thirdparty.dianli.frontend.web.model.base;

import com.google.common.collect.Lists;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * Created by administrator on 17/7/18.
 */
@Entity
@Table(name = "t_menu")
public class Menu  extends  AbstractPersistable<Integer>  {

    @Column(name="pid")
    private Integer parentId;
    @Column(name="name")
    private String label;
    @Column(name="url")
    private String href;
    @Column(name="icon_class")
    private String iconClass;
    @Column(name="idx")
    private Integer sort;
    @Column(name="tenant_id")
    private Integer tenantId;
    @Column(name="resource_id")
    private String resourceId;

    @Transient
    private List<Menu> children= Lists.newArrayList();
    @Transient
    private boolean  active= false;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }

    @Override
    public void setId(Integer id){
          super.setId(id);
    }
    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }


    public String getIconClass() {
        return iconClass;
    }

    public void setIconClass(String iconClass) {
        this.iconClass = iconClass;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

}
