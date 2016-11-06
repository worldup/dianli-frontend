package com.mdata.thirdparty.dianli.frontend.web.services.sensor;

import com.mdata.thirdparty.dianli.frontend.beans.Corporate;
import com.mdata.thirdparty.dianli.frontend.web.controller.api.SensorData;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by administrator on 16/5/15.
 */
public interface SensorService {
      void testConnect();
      List<Map<String,Object>> list(String pkey);
      List<Map<String,Object>>  getDataBetweenTimeRange(String sid, long beginTime, long endTime);
      List<Map<String,Object>>  getKData(String sid, String idx);
      List<Map<String,Object>>  getData(String sid, String idx,String days);
      //绘制2月内 ，按照小时为单位，取每小时的最高和最低点
      Map<String,List<String>>  getRealData(String sid, String idx);
      //绘制每日数据，取样每个点
      Map<String,List<String>>  getRealDataToday(String sid, String idx);
      List<Map<String,Object>>  getThreePhaseData(String aSid,String bSid,String cSid);
      Map<String,List<Map<String, Object>>> getTempHumData(String tSid,String hSid,String tSid1,String hSid1);
      Map<String,List<Map<String, Object>>> getTempCurrentData(String tSid,String hSid);
      Map<String,Map<String,Object>> getSensorDays(String day);
      Map<String,Object> getSensorInfo(String sid);
      void insertSensorValues(List<SensorData> sensorDatas) throws Exception;
      boolean  insertSensorValue(SensorData sensorData) throws Exception;
      List<Corporate> getAllCorporate(int tenantId);
      List<Map<String,String>> getThreephaseSids(String userName );
      List<Map<String,String>> getTempCurrentSids(String userName );
      List<Map<String,String>> getTemperatureSids(String userName );
      List<Map<String,Object>> getSensorDatasByDayAndPage(String day,String userName,int startPage,Integer limit);
      Integer getSensorDatasByDay(String userName,String day);
      List<Map<String,Object>> getSensorDatasByDayAndPageAndSName(String day,String sensorName,String userName,int startPage,Integer limit);
      Integer getSensorDatasByDayAndSName(String day,String sensorName,String userName);
      Integer getWarningDatasCount();
      List<Map<String,Object>> getWarningDatas(int startPage,Integer limit);
      //获取传感器告警配置
      List<Map<String,Object>> getSensorWarningConf(String sid);
      List<Map<String, Object>> listSensorTree(String userName);

}
