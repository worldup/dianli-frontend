package com.mdata.thirdparty.dianli.frontend.web.services.system;

import com.mdata.thirdparty.dianli.frontend.beans.Menu;

import java.util.List;

/**
 * Created by administrator on 16/7/18.
 */
public interface IMenuService {
    List<Menu> listAllMenu(int tenantId);
    List<Menu> listAllMenu(int tenantId,String userName);
}
