package com.mdata.thirdparty.dianli.frontend.web.services.shiro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by administrator on 16/5/15.
 */
public class Role  implements Serializable{


    private String id;

    private String name;
    private String description;

    private List<Permission> permissions;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Permission> getPermissions() {
        if (permissions == null)
            this.permissions = new ArrayList<Permission>();
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

}