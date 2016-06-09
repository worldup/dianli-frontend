package com.mdata.thirdparty.dianli.frontend.migrate;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.CollectionUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by administrator on 16/6/9.
 */
public class Sqlite2Mysql {
    public static void migrateData(JdbcTemplate sqlite,JdbcTemplate mysql,String sensorId){
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
}
