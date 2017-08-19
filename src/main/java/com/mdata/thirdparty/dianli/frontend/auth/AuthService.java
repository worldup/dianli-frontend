package com.mdata.thirdparty.dianli.frontend.auth;

import com.mdata.thirdparty.dianli.frontend.web.model.base.Menu;

import java.util.List;

/**
 * Created by administrator on 16/5/17.
 */
public interface AuthService {
    int getUserTenantId(String userName);
    List<Menu> getMenusByUserId(String userId);
}
