package com.mdata.thirdparty.dianli.frontend.auth;

import com.mdata.thirdparty.dianli.frontend.web.model.base.Menu;
import com.mdata.thirdparty.dianli.frontend.web.services.system.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by administrator on 16/5/17.
 */
@Service
public class AuthServiceImpl implements  AuthService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private IMenuService menuService;
    @Override
    public int getUserTenantId(String userName) {
        return jdbcTemplate.queryForObject("select tenantId from users where username=?", new Object[]{userName}, Integer.class);
    }
    @Override
    public List<Menu> getMenusByUserId(String userId) {

//        Menu menu= menuService.getMenuTreeByTenantId(getUserTenantId(userId));
        Menu menu= menuService.getMenuTreeByUserNameAndTenantId(userId,getUserTenantId(userId));
        return menu.getChildren();
    }
}
