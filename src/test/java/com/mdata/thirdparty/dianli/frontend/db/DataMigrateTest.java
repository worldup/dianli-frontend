package com.mdata.thirdparty.dianli.frontend.db;

import com.mdata.thirdparty.dianli.frontend.ApplicationTest;
import com.mdata.thirdparty.dianli.frontend.migrate.Sqlite2Mysql;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by administrator on 16/6/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(ApplicationTest.class)
public class DataMigrateTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    @Qualifier("sqliteTemplate")
    private JdbcTemplate sqliteJdbcTemplate;
    @Test
    public void testMigrate() {
       // Sqlite2Mysql.migrateSensorData(sqliteJdbcTemplate,jdbcTemplate,"111_05_A_0_00CD01_0");
       Sqlite2Mysql.migrateSensorDays(sqliteJdbcTemplate,jdbcTemplate,"2016");
    }
}
