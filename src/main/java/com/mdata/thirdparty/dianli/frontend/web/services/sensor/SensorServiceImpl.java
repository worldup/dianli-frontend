package com.mdata.thirdparty.dianli.frontend.web.services.sensor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

/**
 * Created by administrator on 16/5/15.
 */
@Service
public class SensorServiceImpl implements  SensorService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public void testConnect() {
        System.out.println("aaaa");
        SqlRowSet sqlRowSet=jdbcTemplate.queryForRowSet("select * from sensor_event limit 1");
        System.out.println(sqlRowSet.getRow());
    }

    @Override
    public void test2() {
        System.out.println(2222);
    }

}
