package com.mdata.thirdparty.dianli.frontend.web.services.sensor;

import com.mdata.thirdparty.dianli.frontend.beans.Corporate;
import com.mdata.thirdparty.dianli.frontend.web.controller.api.SensorData;

import java.util.Date;
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
      List<Map<String,Object>>  getThreePhaseData(String aSid,String bSid,String cSid);
      Map<String,List<Map<String, Object>>> getTempHumData(String tSid,String hSid,String tSid1,String hSid1);
      Map<String,Map<String,Object>> getSensorDays(String day);
      Map<String,Object> getSensorInfo(String sid);
      void insertSensorValues(List<SensorData> sensorDatas) throws Exception;
      void insertSensorValue(SensorData sensorData) throws Exception;
      List<Corporate> getAllCorporate(int tenantId);
      List<Map<String,String>> getThreephaseSids(String userName );
      List<Map<String,String>> getTemperatureSids(String userName );
      List<Map<String,Object>> getSensorDatasByDayAndPage(String day,int startPage,Integer limit);
      Integer getSensorDatasByDay(String day);
      List<Map<String,Object>> getSensorDatasByDayAndPageAndSName(String day,String sensorName,int startPage,Integer limit);
      Integer getSensorDatasByDayAndSName(String day,String sensorName);
}
