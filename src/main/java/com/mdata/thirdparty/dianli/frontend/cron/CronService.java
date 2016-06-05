package com.mdata.thirdparty.dianli.frontend.cron;

import com.mdata.thirdparty.dianli.frontend.beans.SensorDays;
import com.mdata.thirdparty.dianli.frontend.util.DBUtils;
import com.mdata.thirdparty.dianli.frontend.util.TimeUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by administrator on 16/6/4.
 */
@Component
@Configurable
@EnableScheduling
public class CronService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    @Qualifier("sqliteTemplate")
    private JdbcTemplate sqliteJdbcTemplate;
   // @Scheduled(fixedRate = 1000 * 10)
    public void show(){
        System.out.println("begin show");
        Date now=new Date();
//        now.setTime(now.getTime()-1000l*60*60*24*30*2);
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
    public static void main(String[] args) {

    }
}
