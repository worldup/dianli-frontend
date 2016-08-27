package com.mdata.thirdparty.dianli.frontend.web.services.sensor;


import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mdata.thirdparty.dianli.frontend.beans.Corporate;
import com.mdata.thirdparty.dianli.frontend.beans.TSensorDays;
import com.mdata.thirdparty.dianli.frontend.web.controller.api.SensorData;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by administrator on 16/5/15.
 */
@Service
public class SensorServiceImpl implements SensorService, InitializingBean {

    Cache<String, SensorData> lastSensorCache;
    Cache<String, TSensorDays> sensorDaysCache;
    static final String listSql = "select * from t_sensors_group where `parentkey`=?";
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void testConnect() {

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select * from sensor_event limit 1");
        System.out.println(sqlRowSet.getRow());
    }

    @Override
    public List<Map<String, Object>> list(String pkey) {
        if (StringUtils.isBlank(pkey)) {
            pkey = "-1";
        }
        List<Map<String, Object>> result = jdbcTemplate.query(listSql, new Object[]{pkey}, new ColumnMapRowMapper());

        return result;
    }

    @Override
    public List<Map<String, Object>> getDataBetweenTimeRange(String sid, long beginTime, long endTime) {
        String sql = "select * from %s where createtime between ? and ?";
        String finalSql = String.format(sql, sid);
        List<Map<String, Object>> resultMapper = jdbcTemplate.query(finalSql, new Object[]{beginTime, endTime}, new ColumnMapRowMapper());
        return resultMapper;
    }

    @Override
    public List<Map<String, Object>> getKData(String sid, String idx) {
        String sql = "select * from t_sensor_days where sid=? and idx=?";
        List<Map<String, Object>> resultMapper = jdbcTemplate.query(sql, new Object[]{sid, idx}, new ColumnMapRowMapper());
        return resultMapper;
    }
    @Override
    public List<Map<String, Object>> getData(String sid, String idx,String days) {
        String sql = "select  sid,idx,sv,DATE_FORMAT(tmin,'%T') tmin,date_format(tmax,'%T') tmax from t_sensor_data where sid=? and idx=? and days=?";
        List<Map<String, Object>> resultMapper = jdbcTemplate.query(sql, new Object[]{sid, idx,days}, new ColumnMapRowMapper());
        return resultMapper;
    }
    @Override
    public Map<String,List<String>> getRealData(String sid, String idx) {
        String sql = "select  sid,idx,sv,   date_format(tmin,'%y%m%d%H%i') tmin,  date_format(tmax,'%y%m%d%H%i') tmax  from t_sensor_data where sid=? and idx=? and  tmin >DATE_ADD(now(),INTERVAL -3 month)";
        List<Map<String, Object>> resultMapper = jdbcTemplate.query(sql, new Object[]{sid, idx}, new ColumnMapRowMapper());
        Map<String,List<String>> resultMap=Maps.newHashMap();
        List<String> days=Lists.newArrayList();
        List<String> values=Lists.newArrayList();
        resultMap.put("days",days);
        resultMap.put("values",values);
         if(CollectionUtils.isNotEmpty(resultMapper)){
             for(Map<String,Object> tempMap:resultMapper){
                 values.add(MapUtils.getString(tempMap,"sv"));
                 values.add(MapUtils.getString(tempMap,"sv"));
                 days.add(MapUtils.getString(tempMap,"tmax"));
                 days.add(MapUtils.getString(tempMap,"tmin"));
             }
         }
        return resultMap;
    }
    @Override
    public List<Map<String, Object>> getThreePhaseData(String aSid, String bSid, String cSid) {
        String sql = "select sum(case s.type  when 'CurrentA' then  d.smax else 0 end) asmax,\n" +
                "sum(case s.type  when 'CurrentB' then  d.smax else 0 end) bsmax,\n" +
                "sum(case s.type  when 'CurrentC' then  d.smax else 0 end) csmax,\n" +
                "d.days from t_sensor_days  d ,t_sensors s  where d.sid in (?,?, \n" +
                "?) and d.sid=s.sid  group by  d.days";
        List<Map<String, Object>> resultMapper = jdbcTemplate.query(sql, new Object[]{aSid, bSid, cSid}, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
                Map<String, Object> result = new HashMap<String, Object>();
                String days = rs.getString("days");
                double asmax = rs.getDouble("asmax");
                double bsmax = rs.getDouble("bsmax");
                double csmax = rs.getDouble("csmax");
                double max = Math.max(asmax, Math.max(bsmax, csmax));
                double min = Math.min(asmax, Math.min(bsmax, csmax));
                double value = 100d;
                if (min != 0d) {
                    BigDecimal bmax=BigDecimal.valueOf(max);
                    BigDecimal bmin=BigDecimal.valueOf(min);
                    value=  bmax.subtract(bmin).multiply(new BigDecimal(100)).divide(bmin,BigDecimal.ROUND_HALF_UP).setScale(2).doubleValue();
                }
                result.put("days", days);
                result.put("value", String.valueOf(value));
                return result;
            }
        });

        return resultMapper;
    }

    @Override
    public Map<String,List<Map<String, Object>>> getTempHumData(final String tSid, final String hSid, final  String tSid1, final String hSid1) {
        String sql = "SELECT sid,savg, days FROM t_sensor_days\n" +
                "\tWHERE STR_TO_DATE(days, '%Y-%m-%d') > date_add(now(), INTERVAL - 1 YEAR)\n" +
                "\t\tAND sid IN (?,?,?,?)\n" +
                "\t\tAND idx = '0'";

        List<Map<String, Object>> resultMapper = jdbcTemplate.query(sql, new Object[]{tSid, hSid,tSid1,hSid1}, new ColumnMapRowMapper());
        Map<String,List<Map<String,Object>>> result=Maps.newHashMap();
         result.put("tSid",FluentIterable.from(resultMapper).filter(new Predicate<Map<String, Object>>() {
             @Override
             public boolean apply(Map<String, Object> input) {
                 return input.get("sid").equals(tSid);
             }
         }).toList());
        result.put("hSid",FluentIterable.from(resultMapper).filter(new Predicate<Map<String, Object>>() {
            @Override
            public boolean apply(Map<String, Object> input) {
                return input.get("sid").equals(hSid);
            }
        }).toList());
        result.put("tSid1",FluentIterable.from(resultMapper).filter(new Predicate<Map<String, Object>>() {
            @Override
            public boolean apply(Map<String, Object> input) {
                return input.get("sid").equals(tSid1);
            }
        }).toList());
        result.put("hSid1",FluentIterable.from(resultMapper).filter(new Predicate<Map<String, Object>>() {
            @Override
            public boolean apply(Map<String, Object> input) {
                return input.get("sid").equals(hSid1);
            }
        }).toList());
        return result;
    }

    @Override

    public Map<String, Map<String, Object>> getSensorDays(String day) {
        String sql = "select  t.name name  ,d.days days , d.sid sid ,d.idx idx ,d.slast value,t.type type ,count(d.sid) c from t_sensor_days d ,t_sensors t  where d.sid=t.sid\n" +
                "  and d.days=?  group by d.sid order by name ";
        List<Map<String, Object>> resultMapper = jdbcTemplate.query(sql, new Object[]{day}, new ColumnMapRowMapper());
        HashBasedTable<String, String, Map<String, Object>> table = HashBasedTable.create();
        if (CollectionUtils.isNotEmpty(resultMapper)) {
            for (Map<String, Object> map : resultMapper) {
                String sid = MapUtils.getString(map, "sid");
                String idx = MapUtils.getString(map, "idx");
                table.put(sid, idx, map);
            }
        }
        final AtomicInteger aint = new AtomicInteger(0);
        FastDateFormat fdf = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        final String now = fdf.format(new Date());
        Map<String, Map<String, Object>> result = Maps.transformValues(table.column("0"), new Function<Map<String, Object>, Map<String, Object>>() {
            @Override
            public Map<String, Object> apply(Map<String, Object> input) {
                input.put("status", "正常");
                input.put("time", now);
                input.put("id", aint.addAndGet(1));
                return input;
            }
        });
        return result;
    }
    public List<Map<String,Object>> getSensorDatasByDayAndPage(String day,int startPage,Integer limit){
        String sql="select t.sid,t.idx,s.name,t.days,t.update_time,CONCAT(t.sv,type.unit) sv,  case   when \n" +
                "c.type='UpperLimit' and sv>c.value then '异常'\n" +
                "else '正常'\n" +
                "end status,\n" +
                "t.tmax,t.tmin ,c.value from t_sensor_data  t inner join T_SENSORS s\n" +
                "on t.sid=s.sid \n" +
                "left join T_SENSORS_TYPE type on s.type=type.type\n" +
                "left join T_SENSORS_GROUP g \n" +
                "on s.key=g.skey  left join T_SENSORS_GROUP_ALERT_CONF c \n" +
                "on g.key=c.group_key\n" +
                " where   t.tmax =(select max(tmax) from t_sensor_data e where t.days=e.days\n" +
                "\tand t.sid=e.sid and t.idx=e.idx \n" +
                " )   \n" +
                "limit ?,?";
        int startIdx=(startPage-1)*limit;
        int endIdx=startPage*limit;
       return  jdbcTemplate.query(sql,new Object[]{startIdx,endIdx},new ColumnMapRowMapper());
    }
    public List<Map<String,Object>> getSensorDatasByDayAndPageAndSName(String day,String sensorName,int startPage,Integer limit){
        String sql="select t.sid,t.idx,s.name,t.days,t.update_time,CONCAT(t.sv,type.unit) sv,  case   when \n" +
                "c.type='UpperLimit' and sv>c.value then '异常'\n" +
                "else '正常'\n" +
                "end status,\n" +
                "t.tmax,t.tmin,c.value from t_sensor_data  t inner join T_SENSORS s\n" +
                "on t.sid=s.sid \n" +
                "left join T_SENSORS_TYPE type on s.type=type.type\n" +
                "left join T_SENSORS_GROUP g \n" +
                "on s.key=g.skey  left join T_SENSORS_GROUP_ALERT_CONF c \n" +
                "on g.key=c.group_key\n" +
                " where  t.tmax =(select max(tmax) from t_sensor_data e where t.days=e.days\n" +
                "\tand t.sid=e.sid and t.idx=e.idx \n" +
                " )  and s.name like '%"+sensorName+"%' \n" +
                "limit ?,?";
        int startIdx=(startPage-1)*limit;
        int endIdx=startPage*limit;
        return  jdbcTemplate.query(sql,new Object[]{startIdx,endIdx},new ColumnMapRowMapper());
    }
    public Integer getSensorDatasByDay(String day){
        /*String sql="select count(1) from t_sensor_data  t inner join T_SENSORS s\n" +
                "on t.sid=s.sid\n" +
                " where t.days=? \n" +
                "\n" +
                " and t.tmax =(select max(tmax) from t_sensor_data e where t.days=e.days\n" +
                "\tand t.sid=e.sid and t.idx=e.idx \n" +
                " )  \n" ;*/
        String sql="select count(1) from t_sensor_data  t inner join T_SENSORS s\n" +
                "on t.sid=s.sid\n" +
                " where  t.tmax =(select max(tmax) from t_sensor_data e where t.days=e.days\n" +
                "\tand t.sid=e.sid and t.idx=e.idx \n" +
                " )  \n" ;
        return jdbcTemplate.queryForObject(sql,new Object[]{ },Integer.class);
    }
    public Integer getSensorDatasByDayAndSName(String day,String sensorName){
        String sql="select count(1) from t_sensor_data  t inner join T_SENSORS s\n" +
                "on t.sid=s.sid\n" +
                " where t.days=? \n" +
                "\n" +
                " and t.tmax =(select max(tmax) from t_sensor_data e where t.days=e.days\n" +
                "\tand t.sid=e.sid and t.idx=e.idx \n" +
                " )  and s.name like '%"+sensorName+"%' \n" ;
        return jdbcTemplate.queryForObject(sql,new Object[]{day},Integer.class);
    }
    public Map<String, Object> getSensorInfo(String sid) {
        String sql = "select * from t_sensors where sid=?";
        return jdbcTemplate.queryForMap(sql, sid);
    }

    public void insertSensorValues(List<SensorData> sensorDatas) throws Exception {
        if (CollectionUtils.isNotEmpty(sensorDatas)) {
            for (SensorData sensorData : sensorDatas) {
                insertSensorValue(sensorData);
            }
        }
    }

    final String insertSensorDataSql = "insert into t_sensor_data(sid,idx,days,sv,tmax,tmin,create_time) values(?,?,?,?,?,?,now())";

    public void insertSensorValue(final SensorData sensorData) throws  Exception {
        final String sid = sensorData.getSid();
        final int idx = sensorData.getIdx();
        long tg = sensorData.getTg();
        final double sv = sensorData.getSv();

        Date date = new Date(tg);
        final Timestamp times = new Timestamp(date.getTime());
        final String day = FastDateFormat.getInstance("yyyy-MM-dd").format(date);
        final String cacheKey = sid + "@@" + idx + "@@" + day;
        final SensorData cachedData = lastSensorCache.getIfPresent(cacheKey);
        if (cachedData != null && sv == cachedData.getSv()) {
            jdbcTemplate.update("update t_sensor_data set tmax=? ,count=count+1 where id=? ", new Object[]{times, cachedData.getId()});
        } else {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement ps = con.prepareStatement(insertSensorDataSql, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, sid);
                    ps.setInt(2, idx);
                    ps.setString(3, day);
                    ps.setDouble(4, sv);

                    ps.setTimestamp(5, times);
                    ps.setTimestamp(6, times);
                    return ps;
                }
            }, keyHolder);
            long id = keyHolder.getKey().longValue();
            sensorData.setId(id);
            lastSensorCache.put(cacheKey, sensorData);

        }
          TSensorDays cachedSensorDays = sensorDaysCache.getIfPresent(cacheKey);
        if (cachedSensorDays == null) {
            List<TSensorDays> results = jdbcTemplate.query("select * from t_sensor_days where sid=? and idx=? and days=? limit 1", new Object[]{
                    sid, idx, day
            }, new BeanPropertyRowMapper<TSensorDays>(TSensorDays.class));
            if(CollectionUtils.isEmpty(results)){
                cachedSensorDays=null;
            }else{
                cachedSensorDays=results.get(0);
            }
        }
        if(cachedSensorDays==null){

            TSensorDays temp = new TSensorDays();
            temp.setSid(sid);
            temp.setIdx(idx);
            temp.setSchange(1);
            temp.setScount(1);
            temp.setDays(day);
            temp.setSavg(sv);
            temp.setSlast(sv);
            temp.setSmax(sv);
            temp.setSmin(sv);
            temp.setTmax(times);
            temp.setTmin(times);
            temp.setTlast(times);
            jdbcTemplate.update("replace into t_sensor_days (sid,idx,days,smax,smin,savg,slast,tmax,tmin,schange,scount,tlast,create_time) values(?,?,?,?,?,?,?,?,?,?,?,?,now())", new PreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setString(1, sid);
                    ps.setInt(2, idx);
                    ps.setString(3, day);
                    ps.setDouble(4, sv);
                    ps.setDouble(5, sv);
                    ps.setDouble(6, sv);
                    ps.setDouble(7, sv);
                    ps.setTimestamp(8, times);
                    ps.setTimestamp(9, times);
                    ps.setLong(10, 1);
                    ps.setLong(11, 1);
                    ps.setTimestamp(12, times);
                }
            });
            sensorDaysCache.put(cacheKey, temp);
        } else {
            final TSensorDays newCachedSensorDays=cachedSensorDays;
            jdbcTemplate.update("update t_sensor_days  set smax=?,smin=?,savg=?,slast=?,tmax=?,tmin=?,schange=?,scount=?,tlast=? where sid=? and idx=? and days=?", new PreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps) throws SQLException {
                    double slast = newCachedSensorDays.getSlast();
                    double smax = newCachedSensorDays.getSmax();
                    double smin = newCachedSensorDays.getSmin();
                    Timestamp tmax = newCachedSensorDays.getTmax();
                    Timestamp tmin = newCachedSensorDays.getTmin();
                    double savg = newCachedSensorDays.getSavg();
                    long change = newCachedSensorDays.getSchange();
                    long count = newCachedSensorDays.getScount();
                    if (slast != sv) {
                        slast=sv;
                        change = change + 1;
                    }

                    if (slast >= smax) {
                        tmax = times;
                        smax = slast;
                    } else if (slast <= smin) {
                        tmin = times;
                        smin = slast;
                    }
                    count = count + 1;
                    savg = (savg * count + slast) / (count + 1);

                    ps.setDouble(1, smax);
                    ps.setDouble(2, smin);
                    ps.setDouble(3, savg);
                    ps.setDouble(4, slast);
                    ps.setTimestamp(5, tmax);
                    ps.setTimestamp(6, tmin);
                    ps.setLong(7, change);
                    ps.setLong(8, count);
                    ps.setTimestamp(9, times);
                    newCachedSensorDays.setSmax(smax);
                    newCachedSensorDays.setSmin(smin);
                    newCachedSensorDays.setSavg(savg);
                    newCachedSensorDays.setSlast(slast);
                    newCachedSensorDays.setTmax(tmax);
                    newCachedSensorDays.setTmin(tmin);
                    newCachedSensorDays.setSchange(change);
                    newCachedSensorDays.setScount(count);
                    newCachedSensorDays.setTlast(times);
                    ps.setString(10, sid);
                    ps.setInt(11, idx);
                    ps.setString(12, day);
                }

            });
        }
    }



    @Override
    public void afterPropertiesSet() throws Exception {
        lastSensorCache = CacheBuilder.newBuilder().expireAfterWrite(2L, TimeUnit.DAYS).build();
        sensorDaysCache = CacheBuilder.newBuilder().expireAfterWrite(2L, TimeUnit.DAYS).build();
    }
    @Override
    public  List<Corporate> getAllCorporate(int tenantId){
        String sql="select * from t_corporate where tenant_Id=?";
       List<Corporate> corporates=  jdbcTemplate.query(sql,new Object[]{tenantId},new BeanPropertyRowMapper<Corporate>(Corporate.class));
        corporates = Lists.transform(corporates, new Function<Corporate, Corporate>() {
            @Override
            public Corporate apply(Corporate input) {
                input.setLabel(String.format(input.getLabel(), "<span style=\"color: green\">正常</span>"));
                return input;
            }
        });
        return  corporates;
    }

    @Override
    public List<Map<String, String>> getThreephaseSids(String userName) {
        String sql="select tm.* from t_threephase_mapping  tm where tm.resource_id in (\n" +
                "select ga.authority  from group_authorities ga ,\n" +
                "group_members gm  \n" +
                "where gm.username=? and gm.group_id=ga.group_id\n" +
                " \n" +
                ")";
        List<Map<String,Object>> result=jdbcTemplate.query(sql,new Object[]{userName}, new ColumnMapRowMapper());
        if(CollectionUtils.isNotEmpty(result)){
            return Lists.transform(result, new Function<Map<String, Object>, Map<String, String>>() {
                @Override
                public Map<String, String> apply(Map<String, Object> input) {
                    Map<String,String> map=Maps.newHashMap();
                    String sname=MapUtils.getString(input,"sname");
                    map.put("name",String.valueOf(input.get("sname")));
                    String pageHref=new StringBuilder("sensor/chart/threephasek.html?aSid=").append(MapUtils.getString(input,"aphase_sid")).append("&bSid=").append(MapUtils.getString(input,"bphase_sid")).append("&cSid=").append(MapUtils.getString(input,"cphase_sid")).append("&sName=").append(sname).toString();
                    map.put("pageHref",pageHref);
                    return map;
                }
            });
        }
        return Lists.newArrayList();
    }

    @Override
    public List<Map<String, String>> getTemperatureSids(String userName) {
        String sql="select tm.* from t_temperature_mapping  tm where tm.resource_id in (\n" +
                "select ga.authority  from group_authorities ga ,\n" +
                "group_members gm  \n" +
                "where gm.username=? and gm.group_id=ga.group_id\n" +
                " \n" +
                ")";
        List<Map<String,Object>> result=jdbcTemplate.query(sql,new Object[]{userName}, new ColumnMapRowMapper());
        if(CollectionUtils.isNotEmpty(result)){
            return Lists.transform(result, new Function<Map<String, Object>, Map<String, String>>() {
                @Override
                public Map<String, String> apply(Map<String, Object> input) {
                    Map<String,String> map=Maps.newHashMap();
                    String sname=MapUtils.getString(input,"sname");
                    map.put("name",String.valueOf(input.get("sname")));
                    String pageHref=new StringBuilder("sensor/chart/temperaturek.html?sid=").append(MapUtils.getString(input,"sid")).append("&sName=").append(sname).toString();
                    map.put("pageHref",pageHref);
                    return map;
                }
            });
        }
        return Lists.newArrayList();
    }
}
