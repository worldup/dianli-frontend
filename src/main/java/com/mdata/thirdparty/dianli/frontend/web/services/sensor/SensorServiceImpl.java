package com.mdata.thirdparty.dianli.frontend.web.services.sensor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by administrator on 16/5/15.
 */
@Service
public class SensorServiceImpl implements  SensorService {
    static final String listSql="select * from t_sensors_group where `parentkey`=?";
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public void testConnect() {
        System.out.println("aaaa");
        SqlRowSet sqlRowSet=jdbcTemplate.queryForRowSet("select * from sensor_event limit 1");
        System.out.println(sqlRowSet.getRow());
    }

    @Override
    public List<Map<String,Object>> list(String pkey) {
        if(StringUtils.isBlank(pkey)){
            pkey="-1";
        }
       List<Map<String,Object>> result=jdbcTemplate.query(listSql,new Object[]{pkey}, new ColumnMapRowMapper());

        return result;
    }

}
