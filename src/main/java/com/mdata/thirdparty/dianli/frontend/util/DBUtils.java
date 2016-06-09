package com.mdata.thirdparty.dianli.frontend.util;

import com.mdata.thirdparty.dianli.frontend.beans.SensorDays;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


/**
 * Created by administrator on 16/6/4.
 */
public class DBUtils {
    static final String INSERTSQL="insert into `%s` ( `createtime`, `svalue`) values ( %d,'%s')";
    static final String CREATESQL="CREATE TABLE IF NOT EXISTS   `%s` (" +
            " `id` int NOT NULL AUTO_INCREMENT," +
            " `createtime` bigint," +
            " `svalue` double," +
            " PRIMARY KEY (`id`)," +
            " INDEX `idx_%s_createtime` (`createtime`)  " +
            ")  ";
    static final String migrateSQL="replace into SENSOR_DAYS ( `sid`,`idx`,`days`,`smax`,`smin`,`savg`,`slast`,`tmax`,`tmin`,`change`,`count`) values(?,?,?,?,?,?,?,?,?,?,?)";
    public static boolean  createTableIFNotExist(JdbcTemplate jdbcTemplate,String tableName){
        boolean result=true;

        try {
                String sql=String.format(CREATESQL,tableName,tableName);
                jdbcTemplate.execute(sql);
        }catch (Exception e){
            result=false;
            e.printStackTrace();
        }finally {

        }
        return result;
    }
    public static String genInsertSensorDaysSql(String tableName,SensorDays sensorDays,long date){

        String sql=String.format(INSERTSQL,tableName,date,sensorDays.getSlast());
        return sql;
    }
    public static int[] insertSensorDays(JdbcTemplate jdbcTemplate,String [] sql){

        int [] result=jdbcTemplate.batchUpdate(sql);
        return result;
    }
    public static boolean migrateSensorDays(JdbcTemplate jdbcTemplate, final List<SensorDays> sensorDayses,final String date){
        try{

            jdbcTemplate.batchUpdate(migrateSQL, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    SensorDays sensorDays=sensorDayses.get(i);
                    ps.setString(1,sensorDays.getSid());
                    ps.setString(2,sensorDays.getIdx());
                    ps.setString(3, date);
                    ps.setDouble(4,sensorDays.getSmax());
                    ps.setDouble(5,sensorDays.getSmin());
                    ps.setDouble(6,sensorDays.getSavg());
                    ps.setDouble(7,sensorDays.getSlast());
                    ps.setTimestamp(8,new Timestamp(946656000000l+1000l*sensorDays.getTmax()));
                    ps.setTimestamp(9,new Timestamp (946656000000l+1000l*sensorDays.getTmin()));
                    ps.setLong(10,sensorDays.getChange());
                    ps.setLong(11,sensorDays.getCount());
                }

                @Override
                public int getBatchSize() {
                    return sensorDayses.size();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static void main(String[] args) throws  Exception {
       Date date1= DateUtils.parseDate("2010-01-01 10:00:00","yyyy-MM-dd HH:mm:ss");
       Date date2= DateUtils.parseDate("2010-01-01 10:00:02","yyyy-MM-dd HH:mm:ss");
        System.out.println(date2.getTime()-date1.getTime());
    }
}
