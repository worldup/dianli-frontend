package com.mdata.thirdparty.dianli.frontend.web.services.system;

import com.mdata.thirdparty.dianli.frontend.beans.Menu;
import com.mdata.thirdparty.dianli.frontend.beans.TenantLayout;

import java.util.List;

/**
 * Created by administrator on 16/7/18.
 */
public interface IMenuService {
    List<com.mdata.thirdparty.dianli.frontend.web.model.base.Menu> getAllMenus();
    List<com.mdata.thirdparty.dianli.frontend.web.model.base.Menu> getAllMenusByTenantId(Integer tenantId);
    List<com.mdata.thirdparty.dianli.frontend.web.model.base.Menu> getAllMenusByUserNameAndTenantId(String userName,Integer tenantId);
    void addMenu(com.mdata.thirdparty.dianli.frontend.web.model.base.Menu menu);
    com.mdata.thirdparty.dianli.frontend.web.model.base.Menu getMenuTreeByTenantId(Integer tenantId);
    com.mdata.thirdparty.dianli.frontend.web.model.base.Menu getMenuTreeByUserNameAndTenantId(String userName,Integer tenantId);

    List<Menu> listAllMenu(int tenantId);
    List<Menu> listAllMenu(int tenantId,String userName);
    TenantLayout getTenantLayoutById(int tenantId);

    //Integer getSensorWarningCount();
}
