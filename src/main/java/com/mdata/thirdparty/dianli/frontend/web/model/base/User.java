package com.mdata.thirdparty.dianli.frontend.web.model.base;

import java.io.Serializable;

/**
 * Created by administrator on 17/7/17.
 */
public class User implements Serializable {
    private Integer id;
    private String username;
    private String password;
    private Integer tanentId;
    private boolean enabled;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getTanentId() {
        return tanentId;
    }

    public void setTanentId(Integer tanentId) {
        this.tanentId = tanentId;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
