package com.mdata.thirdparty.dianli.frontend.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by administrator on 16/5/17.
 */
@Service
public class AuthServiceImpl implements  AuthService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public int getUserTenantId(String userName) {
        return jdbcTemplate.queryForObject("select tenantId from users where username=?", new Object[]{userName}, Integer.class);
    }
}
