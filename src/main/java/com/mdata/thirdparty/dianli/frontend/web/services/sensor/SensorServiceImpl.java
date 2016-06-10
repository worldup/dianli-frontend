package com.mdata.thirdparty.dianli.frontend.web.services.sensor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
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

    @Override
    public List<Map<String, Object>> getDataBetweenTimeRange(String sid, long beginTime, long endTime) {
        String sql="select * from %s where createtime between ? and ?";
        String finalSql=String.format(sql,sid);
       List<Map<String,Object>>  resultMapper=jdbcTemplate.query(finalSql,new Object[]{beginTime,endTime},new ColumnMapRowMapper()) ;
        return resultMapper;
    }
    @Override
    public List<Map<String,Object>>  getKData(String sid, String idx){
        String sql="select * from sensor_days where sid=? and idx=?";
        List<Map<String,Object>> resultMapper=jdbcTemplate.query(sql,new Object[]{sid,idx},new ColumnMapRowMapper());
        return resultMapper;
    }
    @Override
    public List<Map<String,Object>>  getThreePhaseData(String aSid,String bSid,String cSid){
        String sql="select sum(case s.type  when 'CurrentA' then  d.smax else 0 end) asmax,\n" +
                "sum(case s.type  when 'CurrentB' then  d.smax else 0 end) bsmax,\n" +
                "sum(case s.type  when 'CurrentC' then  d.smax else 0 end) csmax,\n" +
                "d.days from sensor_days  d ,t_sensors s  where d.sid in (?,?, \n" +
                "?) and d.sid=s.sid  group by  d.days";
        List<Map<String,Object>> resultMapper=jdbcTemplate.query(sql, new Object[]{aSid, bSid, cSid}, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
                Map<String,Object> result=new HashMap<String, Object>();
                String days=rs.getString("days");
                double asmax=rs.getDouble("asmax");
                double bsmax=rs.getDouble("bsmax");
                double csmax=rs.getDouble("csmax");
                double max=Math.max(asmax,Math.max(bsmax,csmax));
                double min=Math.min(asmax,Math.min(bsmax,csmax));
                double value=1d;
                if(min!=0d){
                    value=((max-min)/min-1);
                }
                result.put("days",days);
                result.put("value",String.valueOf(value));
                return result;
            }
        });

        return resultMapper;
    }

    @Override
    public List<Map<String, Object>> getTempHumData(String tSid, String hSid) {
       String sql=  "select sum(case s.type  when 'Humidity' then  d.savg else 0 end) havg,\n" +
               " sum(case s.type  when 'Temperature' then  d.savg else 0 end) tavg,\n" +
               " d.days from sensor_days  d ,t_sensors s \n" +
               " where d.sid in (?,?) and d.sid=s.sid \n" +
               " group by  d.days";
        List<Map<String,Object>> resultMapper=jdbcTemplate.query(sql, new Object[]{tSid, hSid}, new ColumnMapRowMapper() );

        return resultMapper;
    }

}
