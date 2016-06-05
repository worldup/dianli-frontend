package com.mdata.thirdparty.dianli.frontend.db;

import com.mdata.thirdparty.dianli.frontend.ApplicationTest;
import com.mdata.thirdparty.dianli.frontend.migrate.SensorTableGen;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by administrator on 16/6/5.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(ApplicationTest.class)
public class MigrateTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Test
    public void migrateSensors(){
        SensorTableGen.migrateSensors(jdbcTemplate);
    }
    @Test
    public void migrateSensorGroup(){
        SensorTableGen.migrateSensorGroup(jdbcTemplate);
    }
    @Test
    public void migrateSensorGroupAlertConfig(){
        SensorTableGen.migrateSensorGroupAlertConfig(jdbcTemplate);
    }


}
