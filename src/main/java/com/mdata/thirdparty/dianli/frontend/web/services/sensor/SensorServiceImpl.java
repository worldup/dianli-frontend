package com.mdata.thirdparty.dianli.frontend.web.services.sensor;


import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
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
                double value = 1d;
                if (min != 0d) {
                    value = ((max - min) / min - 1);
                }
                result.put("days", days);
                result.put("value", String.valueOf(value));
                return result;
            }
        });

        return resultMapper;
    }

    @Override
    public List<Map<String, Object>> getTempHumData(String tSid, String hSid) {
        String sql = "select sum(case s.type  when 'Humidity' then  d.savg else 0 end) havg,\n" +
                " sum(case s.type  when 'Temperature' then  d.savg else 0 end) tavg,\n" +
                " d.days from t_sensor_days  d ,t_sensors s \n" +
                " where d.sid in (?,?) and d.sid=s.sid \n" +
                " group by  d.days";
        List<Map<String, Object>> resultMapper = jdbcTemplate.query(sql, new Object[]{tSid, hSid}, new ColumnMapRowMapper());

        return resultMapper;
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
}
