package com.mdata.thirdparty.dianli.frontend.web.services.shiro;

import java.io.Serializable;

/**
 * Created by administrator on 16/5/15.
 */
public class Permission  implements Serializable{
    private String id;

    private String name;
    private String description;

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

}