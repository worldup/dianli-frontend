package com.mdata.thirdparty.dianli.frontend.forecast;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.*;
import com.mdata.thirdparty.dianli.frontend.web.model.BileiqiSensor;
import com.mdata.thirdparty.dianli.frontend.web.model.BileiqiSensorMapping;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ColumnMapRowMapper;
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
    //获取雷击次数不超过阈值的传感器
    public Set<String> getCommonLJSensors(){
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
                return v<ljCount;
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
    public Set<String> getSinglePhaseFailure(){
        Map<String ,Integer> tempMap=Maps.newHashMap();
        Set<String> result=Sets.newHashSet();
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
                    result.add(entry.getKey());
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
        Set<String> singleFailures=getSinglePhaseFailure();
        List<BileiqiSensorMapping> bileiqiSensorMappings= getBileiqiSensorMapping();
        Map<String,Integer> result=Maps.newHashMap();
        Set<String > failurePoles=Sets.newHashSet();
        if(CollectionUtils.isNotEmpty(singleFailures)){
            for(String key:singleFailures){
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

    //隐患检测 -阀片老化
    public Set<String> getAgingSensors(){

        Map<String ,Integer> tempMap=Maps.newHashMap();
       Set<String> result=Sets.newHashSet();
        List<BileiqiSensorMapping> bileiqiSensorMappings= getBileiqiSensorMapping();
        Set<String>  wdSensors=getUnCommonWDSensors();
        Set<String>  dlSensors=getUnCommonDLSensors();
        Set<String> ljSensors=getCommonLJSensors();

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
                    result.add(entry.getKey());
                }
            }
        }
        return result;
    }
    //缺陷检测 -爬电/漏电
    public Set<String> getreepageSensors(){

        Map<String ,Integer> tempMap=Maps.newHashMap();
        Set<String> result=Sets.newHashSet();
        List<BileiqiSensorMapping> bileiqiSensorMappings= getBileiqiSensorMapping();
        Set<String>  wdSensors=getUnCommonWDSensors();
        Set<String>  dlSensors=getUnCommonDLSensors();

        if(CollectionUtils.isNotEmpty(wdSensors)&&CollectionUtils.isNotEmpty(dlSensors)){
            for(String wdSensor:wdSensors){
                String poleAndType= getPoleType(bileiqiSensorMappings,wdSensor,"wd");
                putAndIncrease(tempMap,poleAndType);
            }
            for(String dlSensor:dlSensors){
                String poleAndType= getPoleType(bileiqiSensorMappings,dlSensor,"dl");
                putAndIncrease(tempMap,poleAndType);
            }

            for(Map.Entry<String,Integer> entry:tempMap.entrySet()){
                if(entry.getValue()==2){
                    result.add(entry.getKey());
                }
            }
        }
        return result;
    }
    //缺陷检测 -底座接地不良
    public  List<String> getZeroDlSensors(){
        Double dlZero=bileiqiUnCommonConfigBean.getDlZero();
        long delay= bileiqiUnCommonConfigBean.getDl().getDelay();
        TimeUnit.Unit unit= bileiqiUnCommonConfigBean.getDl().getUnit();
        TimeUnit timeUnit=new TimeUnit(delay,unit);
        String sql=   "select  sid     from t_sensor_days where sid in( " +
                " " +
                "select bl_dl_sid from t_bileiqi_sensors_mapping  " +
                ") " +
                "and    days > date_sub(now(),interval "+timeUnit.toString()+") " +
                "group by sid having savg <= " + dlZero;
        List<String> stringList=  jdbcTemplate.query(sql, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int i) throws SQLException {


                String sid=rs.getString("sid");
                return sid;
            }
        });
        return stringList;
    }

    //缺陷检测 -放电不计数
    public Set<String> getUnCalcLj(){
        String sql= 
                "select  t.pole ,GROUP_CONCAT(t.savg) savg from (  " +
                "select sm.pole ,avg(sd.savg) savg ,sd.sid  from t_sensor_days  sd join   t_bileiqi_sensors_mapping sm   " +
                "on sd.sid =sm.bl_lj_sid   " +
                "where sd.days > date_sub(now(),interval 20 day)  " +
                "group by sd.sid ) t  " +
                "group by t.pole";

        List<Map<String,String>> mapList=  jdbcTemplate.query(sql, new RowMapper<Map<String, String>>() {
            @Override
            public Map<String, String> mapRow(ResultSet rs, int i) throws SQLException {
                Map<String, String> map= Maps.newHashMap();
                String savg= rs.getString("savg");
                String pole=rs.getString("pole");
                map.put("pole",pole);
                map.put("savg",savg);
                return map;
            }
        });
        if(CollectionUtils.isNotEmpty(mapList)){
            Set<Map<String,String>> mapSet= Sets.newHashSet(Iterables.filter(mapList, new Predicate<Map<String, String>>() {
                @Override
                public boolean apply(Map<String, String> stringObjectMap) {
                    String savg=  stringObjectMap.get("savg");

                    return isUnCalc(savg);
                }
            }).iterator());
            if(CollectionUtils.isNotEmpty(mapList)){
              return   Sets.newHashSet(Iterables.transform(mapSet, new Function<Map<String,String>, String>() {
                    @Override
                    public String apply(Map<String, String> stringStringMap) {
                        return stringStringMap.get("pole");
                    }
                })
                );
            }
        }
        return null;
    }

    public boolean isUnCalc(String str){
        if(StringUtils.isNotEmpty(str)&&str.contains(",")){
            List <String> strList=Lists.newArrayList(Splitter.on(",").split(str));
            if(strList.size()==3){
                try{
                  BigDecimal  d1=BigDecimal.valueOf(Double.valueOf(strList.get(0)))  ;
                  BigDecimal  d2=BigDecimal.valueOf(Double.valueOf(strList.get(1)))  ;
                  BigDecimal  d3=BigDecimal.valueOf(Double.valueOf(strList.get(2)))  ;

                 BigDecimal avg=  d1.add(d2).add(d3).divide(new BigDecimal(3),BigDecimal.ROUND_HALF_UP);
                 if(avg.doubleValue()>1){
                     if(d1.divide(avg,BigDecimal.ROUND_HALF_UP).doubleValue()<0.1d||d2.divide(avg,BigDecimal.ROUND_HALF_UP).doubleValue()<0.1d){
                         return true;
                     }
                     return false;
                 }
                }catch (Exception e){
                    return true;
                }

            }
        }
        return true;
    }

    //查询传感器是否异常,如果近一天没有数据则需要告警
    public Set<Map<String,Object>> getUnCommonSensor(){
        long delay= bileiqiUnCommonConfigBean.getDelay();
        TimeUnit.Unit unit= bileiqiUnCommonConfigBean.getUnit();
        TimeUnit timeUnit=new TimeUnit(delay,unit);
        String sql="select a.pole,a.ptype,a.stype,a.sid,avg(sd.savg) savg  from (  " +
                "select pole,type ptype, 'wd' stype, bl_wd_sid sid  from t_bileiqi_sensors_mapping  " +
                "UNION  " +
                "select  pole,type ptype, 'lj' stype,bl_lj_sid  sid from t_bileiqi_sensors_mapping  " +
                "UNION  " +
                "select  pole,type ptype,'dl' stype, bl_dl_sid  sid from t_bileiqi_sensors_mapping  " +
                "UNION  " +
                "select  pole,type ptype, 'twd' stype, tq_wd_sid sid from t_bileiqi_sensors_mapping  " +
                "UNION  " +
                "select  pole,type ptype,'tsd' stype, tq_sd_sid sid  from t_bileiqi_sensors_mapping  " +
                ")a left join t_sensor_days sd  " +
                "on a.sid =sd.sid   " +
                "and  sd.days > date_sub(now(),interval "+timeUnit.toString()+") group by a.pole,a.ptype,a.stype";
        List<Map<String,Object>> mapList=  jdbcTemplate.query(sql,  new ColumnMapRowMapper());
        if(CollectionUtils.isNotEmpty(mapList)){
            Set<Map<String,Object>> mapSet=Sets.newHashSet(Iterables.filter(mapList, new Predicate<Map<String, Object>>() {
                @Override
                public boolean apply(Map<String, Object> stringStringMap) {
                    return stringStringMap.get("savg")==null;
                }
            }));
            if(CollectionUtils.isNotEmpty(mapSet)){
               return mapSet;
            }
        }
        return null;
    }

}
