package com.mdata.thirdparty.dianli.frontend.web.services.system;

import com.mdata.thirdparty.dianli.frontend.beans.Menu;
import com.mdata.thirdparty.dianli.frontend.beans.TenantLayout;

import java.util.List;

/**
 * Created by administrator on 16/7/18.
 */
public interface IMenuService {
    List<Menu> listAllMenu(int tenantId);
    List<Menu> listAllMenu(int tenantId,String userName);
    TenantLayout getTenantLayoutById(int tenantId);

    //Integer getSensorWarningCount();
}
