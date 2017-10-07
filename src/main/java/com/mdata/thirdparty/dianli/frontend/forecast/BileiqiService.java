package com.mdata.thirdparty.dianli.frontend.forecast;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.*;
import com.mdata.thirdparty.dianli.frontend.web.model.BileiqiSensor;
import com.mdata.thirdparty.dianli.frontend.web.model.BileiqiSensorMapping;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class BileiqiService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private BileiqiUnCommonConfigBean bileiqiUnCommonConfigBean;
    //获取避雷器配置
    public List<BileiqiSensorMapping> getBileiqiSensorMapping(){
        String sql="select * from t_bileiqi_sensors_mapping";
        List<BileiqiSensorMapping> bileiqiSensorMappings= jdbcTemplate.query(sql,new BeanPropertyRowMapper<BileiqiSensorMapping>(BileiqiSensorMapping.class));
        return bileiqiSensorMappings;
    }
    //按照传感器类型获取timeUnit平均值
    public  List<Map<String,Object>> getAvgNDaysAgoGropBySid(String sidTypeColumn,TimeUnit timeUnit){
        String sql=   "select avg(savg) savg ,sid  sid  from t_sensor_days where sid in( " +
                " " +
                "select "+ sidTypeColumn +" from t_bileiqi_sensors_mapping  " +
                ") " +
                "and    days > date_sub(now(),interval "+timeUnit.toString()+") " +
                "group by sid ";
       List<Map<String,Object>> mapList=  jdbcTemplate.query(sql, new RowMapper<Map<String, Object>>() {
           @Override
           public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
               Map<String, Object> map= Maps.newHashMap();
                Double savg= rs.getDouble("savg");
                String sid=rs.getString("sid");
                map.put("sid",sid);
                map.put("savg",savg);
               return map;
           }
       });
       return mapList;
    }
    //获取传感器30日平均值

    public  Double  getAvg30DaysAgoAllSid(String sidTypeColumn){
        String sql=   "select avg(savg) savg    from t_sensor_days where sid in( " +
                " " +
                "select "+sidTypeColumn +" from t_bileiqi_sensors_mapping  " +
                ") " +
                "and    days > date_sub(now(),interval  30 DAY " +") " ;
         Double  avg=  jdbcTemplate.queryForObject(sql,Double.class);
        return avg;
    }
    //d1 标准值 d2 测量值 rangeRate 区间比例
    private boolean unCommon(Double d1,Double d2,Double rangeRate){
        if(d1!=null && d2!=null && rangeRate!=null){
            BigDecimal  bd2= BigDecimal.valueOf(d2);
            BigDecimal bd1= BigDecimal.valueOf(d1);
            BigDecimal bRangeRate=BigDecimal.valueOf(rangeRate);
            try{
                double flag= bd2.subtract(bd1).divide(bd1,BigDecimal.ROUND_HALF_UP).abs().subtract(bRangeRate).doubleValue();
                return flag>0;
            }catch (Exception e){
                e.printStackTrace();
                return true;
            }

        }
        return true;
    }
    public Set<String> getUnCommonSensors(String sidTypeColumn, Double rangeRate, TimeUnit timeUnit){
        Set<String> result= Sets.newHashSet();
        List<Map<String,Object>>   mapList=getAvgNDaysAgoGropBySid(sidTypeColumn,timeUnit);
        Double avg=getAvg30DaysAgoAllSid(sidTypeColumn);
        if(CollectionUtils.isNotEmpty(mapList)){
            for(Map<String,Object> map:mapList){
              String sid= MapUtils.getString(map,"sid");
              Double savg= MapUtils.getDouble(map,"savg");
              if(unCommon(avg,savg,rangeRate)){
                  result.add(sid);
              }
            }
        }
        return result;
    }
    //获取
    //获取异常电流传感器
    public Set<String> getUnCommonDLSensors(){
        String sidTypeCol="bl_dl_sid";
        Double rangeRate=bileiqiUnCommonConfigBean.getDl().getPercent();
        long delay= bileiqiUnCommonConfigBean.getDl().getDelay();
        TimeUnit.Unit unit= bileiqiUnCommonConfigBean.getDl().getUnit();
        TimeUnit timeUnit=new TimeUnit(delay,unit);
        return getUnCommonSensors(sidTypeCol,rangeRate,timeUnit);
    }
    //获取异常管芯温度传感器
    public Set<String> getUnCommonWDSensors(){
        String sidTypeCol="bl_wd_sid";
        Double rangeRate=bileiqiUnCommonConfigBean.getWd().getPercent();
        long delay= bileiqiUnCommonConfigBean.getWd().getDelay();
        TimeUnit.Unit unit= bileiqiUnCommonConfigBean.getWd().getUnit();
        TimeUnit timeUnit=new TimeUnit(delay,unit);
        return getUnCommonSensors(sidTypeCol,rangeRate,timeUnit);
    }
    //获取雷击次数超标传感器
    public Set<String> getUnCommonLJSensors(){
        String sidTypeCol="bl_lj_sid";
        final long  ljCount=bileiqiUnCommonConfigBean.getLj().getCount();
        long  delay=bileiqiUnCommonConfigBean.getLj().getDelay();
        TimeUnit.Unit unit=bileiqiUnCommonConfigBean.getLj().getUnit();
        TimeUnit timeUnit=new TimeUnit(delay,unit);
        List<Map<String,Object>>   mapList=getAvgNDaysAgoGropBySid(sidTypeCol,timeUnit);
        Iterable<Map<String,Object>> uncommonMapLists=  Iterables.filter(mapList, new Predicate<Map<String, Object>>() {
            @Override
            public boolean apply(Map<String, Object> stringObjectMap) {
                long v=  MapUtils.getLong(stringObjectMap,"savg",0l);
                return v>=ljCount;
            }
        });
        if(uncommonMapLists !=null  &&uncommonMapLists.iterator().hasNext()){
            return Sets.newHashSet(Collections2.transform(Lists.newArrayList(uncommonMapLists), new Function<Map<String,Object>, String>() {
                @Override
                public String apply(Map<String, Object> stringObjectMap) {
                    return MapUtils.getString(stringObjectMap,"sid");
                }
            }).iterator()) ;
        }
        return null;

    }
    //故障监测 1.单相击穿故障监测
    public Map<String,Integer> getSinglePhaseFailure(){
        Map<String ,Integer> tempMap=Maps.newHashMap();
        Map<String ,Integer> result=Maps.newHashMap();
        List<BileiqiSensorMapping> bileiqiSensorMappings= getBileiqiSensorMapping();
        Set<String>  wdSensors=getUnCommonWDSensors();
        Set<String>  dlSensors=getUnCommonDLSensors();
        Set<String> ljSensors=getUnCommonLJSensors();

        if(CollectionUtils.isNotEmpty(wdSensors)&&CollectionUtils.isNotEmpty(dlSensors)&&CollectionUtils.isNotEmpty(ljSensors)){
          for(String wdSensor:wdSensors){
              String poleAndType= getPoleType(bileiqiSensorMappings,wdSensor,"wd");
              putAndIncrease(tempMap,poleAndType);
          }
            for(String dlSensor:dlSensors){
                String poleAndType= getPoleType(bileiqiSensorMappings,dlSensor,"dl");
                putAndIncrease(tempMap,poleAndType);
            }
            for(String ljSensor:ljSensors){
                String poleAndType= getPoleType(bileiqiSensorMappings,ljSensor,"lj");
                putAndIncrease(tempMap,poleAndType);
            }
            for(Map.Entry<String,Integer> entry:tempMap.entrySet()){
                if(entry.getValue()==3){
                    result.put(entry.getKey(),entry.getValue());
                }
            }
        }
        return result;
    }
    private void putAndIncrease(Map<String,Integer> map,String key){
        if(!map.containsKey(key)){
            map.put(key,new Integer(1));
        }
        else{
            map.put(key,new Integer(map.get(key)+1));
        }
    }
    private String getPoleType( List<BileiqiSensorMapping> sensorMappings,String sid,String sidType){
        for(BileiqiSensorMapping bileiqiSensorMapping:sensorMappings){
             String tsid="";
            if("wd".equals(sidType)){
                tsid=bileiqiSensorMapping.getBlWdSid();
            }
            else if("dl".equals(sidType)){
                tsid=bileiqiSensorMapping.getBlDlSid();
            }
            else if("lj".equals(sidType)){
                tsid=bileiqiSensorMapping.getBlLjSid();
            }
            if(tsid.equals(sid)){
                return bileiqiSensorMapping.getPole()+"@"+bileiqiSensorMapping.getType();
            }
        }
        return null;
    }
    //故障监测 1.多相击穿故障监测
    public Set<String> getMultiPhaseFailure(){
        Map<String,Integer> singleFailures=getSinglePhaseFailure();
        List<BileiqiSensorMapping> bileiqiSensorMappings= getBileiqiSensorMapping();
        Map<String,Integer> result=Maps.newHashMap();
        Set<String > failurePoles=Sets.newHashSet();
        if(MapUtils.isNotEmpty(singleFailures)){
            for(String key:singleFailures.keySet()){
                List<String> poleAndType= Lists.newArrayList(Splitter.on("@").split(key).iterator());
                if(CollectionUtils.isNotEmpty(poleAndType)&&poleAndType.size()==2){
                   String pole= poleAndType.get(0);
                   putAndIncrease(result,pole);
                }
            }
        }
        for(Map.Entry<String,Integer> resultEntry:result.entrySet()){
            if(resultEntry.getValue()>1){
                failurePoles.add(resultEntry.getKey());
            }
        }
        return failurePoles;
    }

}
