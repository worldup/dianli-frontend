package com.mdata.thirdparty.dianli.frontend.migrate;

import com.mdata.thirdparty.dianli.frontend.beans.SensorDays;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.CollectionUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by administrator on 16/6/9.
 */
public class Sqlite2Mysql {
    public static void migrateSensorData(JdbcTemplate sqlite, JdbcTemplate mysql, String sensorId){
      final List<Map<String,Object>> result=  sqlite.query(String.format("select * from %s","S"+sensorId),new Object[]{},new ColumnMapRowMapper());
        if(!CollectionUtils.isEmpty(result)){

            final String insertSql="replace into T"+sensorId+" (  `createtime`,`svalue`) values(?,?)";
            final List<Map<String,Object>> insertResult=new ArrayList<Map<String, Object>>();
             for(Map<String,Object> map:result){
                 long tbegin=Long.parseLong(map.get("tbegin")+"");
                 long tend=Long.parseLong(map.get("tend")+"");
                 String svalue=map.get("svalue")+"";
                     Map<String,Object > beginMap=new HashMap<String, Object>();
                 beginMap.put("createtime",tbegin);
                 beginMap.put("svalue",svalue);
                     insertResult.add(beginMap);
                 Map<String,Object > endMap=new HashMap<String, Object>();
                 endMap.put("createtime",tend);
                 endMap.put("svalue",svalue);
                 insertResult.add(endMap);

             }

            mysql.batchUpdate(insertSql, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Map<String,Object > map =insertResult.get(i);
                        long value=Long.parseLong(map.get("createtime")+"")*1000+946684800000l;
                        ps.setLong(1,value);
                        ps.setString(2,map.get("svalue")+"");


                    }

                    @Override
                    public int getBatchSize() {
                        return insertResult.size();
                    }
                });

        }
    }
    public static void migrateSensorDays(JdbcTemplate sqlite, JdbcTemplate mysql,String year){
        final   List<SensorDays> result=  sqlite.query("select * from sensor_days",new Object[]{},new BeanPropertyRowMapper(SensorDays.class));
        if(!CollectionUtils.isEmpty(result)){

            final String insertSql="replace into  sensor_days (  `sid`,`idx`,`days`,`smax`,`smin`,`savg`,`slast`,`tmax`,`tmin`,`change`,`count`) values(?,?,?,?,?,?,?,?,?,?,?)";
            final List<Map<String,Object>> insertResult=new ArrayList<Map<String, Object>>();

            try{
                final Date mYear=DateUtils.parseDate(year+"0101","yyyyMMdd");

                mysql.batchUpdate(insertSql, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        SensorDays sensorDays=result.get(i);
                        ps.setString(1,sensorDays.getSid());
                        ps.setString(2,sensorDays.getIdx());
                        ps.setString(3, DateFormatUtils.format(DateUtils.addDays(mYear,sensorDays.getDays()-1),"yyyy-MM-dd"));
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
                        return result.size();
                    }
                });
            }catch (Exception e){
                e.printStackTrace();

            }


        }
    }
}
