package com.mdata.thirdparty.dianli.frontend.db;
import com.mdata.thirdparty.dianli.frontend.Application;
import com.mdata.thirdparty.dianli.frontend.ApplicationTest;
import com.mdata.thirdparty.dianli.frontend.beans.SensorDays;
import com.mdata.thirdparty.dianli.frontend.util.DBUtils;
import com.mdata.thirdparty.dianli.frontend.util.TimeUtils;
import jdk.nashorn.internal.scripts.JD;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.Date;
import java.util.List;

/**
 * Created by administrator on 16/6/4.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(ApplicationTest.class)
public class DBUtilsTest {


    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    @Qualifier("sqliteTemplate")
    private JdbcTemplate sqliteJdbcTemplate;
    @Test
    public void testTableCreate(){
        Assert.assertEquals(DBUtils.createTableIFNotExist(jdbcTemplate,"S211_04_A_0_00E182_0"),true);;
    }
    @Test
    public void testMigrate(){
        Date now=new Date();
        String  datetime= DateFormatUtils.format(now,"yyyy-MM-dd HH:mm:ss");
        String  date= DateFormatUtils.format(now,"yyyy-MM-dd");
        int dayOfYear= TimeUtils.getDayOfYear(now);
       List<SensorDays> sensorDaysList= sqliteJdbcTemplate.query("select * from sensor_days where days=?",new Object[]{dayOfYear}, new BeanPropertyRowMapper(SensorDays.class));
       if(!CollectionUtils.isEmpty(sensorDaysList)){
           String[] insertSql=new String[sensorDaysList.size()];
           int i=0;
           for(SensorDays sensorDays:sensorDaysList){
               StringBuilder sbTable=new StringBuilder();
               sbTable.append("T").append(sensorDays.getSid()).append("_").append(sensorDays.getIdx());
               String tableName=sbTable.toString();
               //如果不存在就先创建表
               DBUtils.createTableIFNotExist(jdbcTemplate,tableName);
               //生成插入表数据语句
               insertSql[i++]=DBUtils.genInsertSensorDaysSql(tableName,sensorDays,datetime);
           }
           DBUtils.migrateSensorDays(jdbcTemplate,sensorDaysList,date);
            DBUtils.insertSensorDays(jdbcTemplate,insertSql);
       }

    }
}
